package com.example.vaxpet

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.vaxpet.databinding.ActivityMantMascotaBinding
import com.example.vaxpet.views.DatePickerFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class MantMascotaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMantMascotaBinding
    private lateinit var firebaseDatabase: FirebaseDatabase

    private var id: String = ""
    private var idPropietario: String = ""
    private var nombre: String = ""
    private var raza: String = ""
    private var sexo: String = ""
    private var fecha_nacimiento: String = ""
    private var descripcion: String = ""
    private var tipo: String = ""
    private var imagen: String = ""
    private var imageUri: Uri = Uri.EMPTY

    private var isCreate: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMantMascotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idPropietario = intent.getStringExtra("idPropietario").toString()
        id = intent.getStringExtra("idMascota").toString()
        isCreate = intent.getBooleanExtra("isCreate", true)

        firebaseDatabase = FirebaseDatabase.getInstance()

        if (isCreate) {
            binding.btRegistrar.setText(R.string.guardar)
        } else {
            binding.btRegistrar.setText(R.string.actualizar)
            obtenerMascota()
        }

         binding.etFechaNacimiento.setOnClickListener{
            showDatePickerDialog()
         }


        // Cuando se da click en registrar
        binding.btRegistrar.setOnClickListener {
            nombre = binding.etNombreMascota.text.toString()
            raza = binding.etRaza.text.toString()
            sexo = binding.etSexo.text.toString()
            fecha_nacimiento = binding.etFechaNacimiento.text.toString()
            descripcion = binding.etDescripcion.text.toString()
            tipo = binding.etTipo.text.toString()

            if (isValidPet()) {
                if (isCreate) {
                    createMascota()
                } else {
                    updateMascota()
                }

            }
        }

        // Cuando se da click en cancelar
        binding.btCancelar.setOnClickListener {
            if (isCreate) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                // Regresar a perfil de mascota
                val intent = Intent(this, PerfilMascotaActivity::class.java)
                intent.putExtra("idMascota", id)
                startActivity(intent)
            }
            finish()
        }

        // Cuando se da click en seleccionar imagen
        binding.ibSelecionarImagen.setOnClickListener {
            launchGallery()
        }


    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun onDateSelected(day: Int, month: Int, year: Int){
        binding.etFechaNacimiento.setText("$day/$month/$year")
    }


    private fun isValidPet(): Boolean {
        var isValid: Boolean = true

        if (nombre.isEmpty()) {
            binding.etNombreMascota.setError("Ingrese el nombre de la mascota")
            isValid = false
        }
        if (tipo.isEmpty()) {
            binding.etRaza.setError("Ingrese el tipo de mascota")
            isValid = false
        }
        if (raza.isEmpty()) {
            binding.etRaza.setError("Ingrese la raza de la mascota")
            isValid = false
        }
        if (sexo.isEmpty()) {
            binding.etSexo.setError("Ingrese el sexo de la mascota")
            isValid = false
        }
        if (fecha_nacimiento.isEmpty()) {
            binding.etFechaNacimiento.setError("Ingrese la fecha de nacimiento de la mascota")
            isValid = false
        }
        if (descripcion.isEmpty()) {
            binding.etDescripcion.setError("Ingrese una descripción de la mascota")
            isValid = false
        }

        return isValid
    }

    private fun obtenerMascota() {
        firebaseDatabase.getReference("mascotas").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val mascotas = snapshot.children.mapNotNull {
                        it.getValue(mascotaData::class.java)
                    }.toList()

                    for (data in mascotas)
                    {
                        if(data.getId() == id){
                            binding.etNombreMascota.setText(data.getNombre().toString())
                            binding.etRaza.setText(data.getRaza().toString())
                            binding.etSexo.setText(data.getSexo().toString())
                            binding.etFechaNacimiento.setText(data.getFechaNacimiento().toString())
                            binding.etDescripcion.setText(data.getDescripcion().toString())
                            binding.etTipo.setText(data.getTipo().toString())
                            Glide.with(this@MantMascotaActivity)
                                .load(data.getPath())
                                .into(binding.ivMascotaImage)
                            imagen = data.getImagen()!!
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error", error.message)
            }
        })
    }

    private fun createMascota() {
        var mascotaData: mascotaData
        mascotaData = mascotaData()

        uploadImage()

        var time = System.currentTimeMillis()
        mascotaData.setId(time.toString())
        mascotaData.setIdPropietario(idPropietario)
        mascotaData.setNombre(nombre)
        mascotaData.setRaza(raza)
        mascotaData.setSexo(sexo)
        mascotaData.setFechaNacimiento(fecha_nacimiento)
        mascotaData.setDescripcion(descripcion)
        mascotaData.setTipo(tipo)
        mascotaData.setImagen(imagen)

        firebaseDatabase.getReference().child("mascotas").child(mascotaData.getId().toString()).setValue(mascotaData)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Mascota registrada correctamente", Toast.LENGTH_SHORT)
                        .show()
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Ocurrio un problema al registrar la mascota, intentelo mas tarde",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun updateMascota() {
        var mascotaData: mascotaData
        mascotaData = mascotaData()

        uploadImage()

        mascotaData.setId(id)
        mascotaData.setIdPropietario("HRm1DvtEODVFwwuL6ffSr5LfR9b2")
        mascotaData.setNombre(nombre)
        mascotaData.setRaza(raza)
        mascotaData.setSexo(sexo)
        mascotaData.setFechaNacimiento(fecha_nacimiento)
        mascotaData.setDescripcion(descripcion)
        mascotaData.setTipo(tipo)
        mascotaData.setImagen(imagen)

        firebaseDatabase.getReference().child("mascotas").child(id).setValue(mascotaData)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Informacion actualizada correctamente", Toast.LENGTH_SHORT)
                        .show()
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Ocurrio un problema al actualizar la informacion de la mascota, intentelo mas tarde",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun uploadImage() {
        if (!Uri.EMPTY.equals(imageUri)) {
            val random = java.util.Random()
            val fileName = "pet00-" + random.nextInt().toString()
            imagen = fileName
            val storageReference = FirebaseStorage.getInstance().getReference("images/pet/" + fileName)
            storageReference.putFile(imageUri).addOnSuccessListener {
            }
        }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "images/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            binding.ivMascotaImage.setImageURI(imageUri)
        }
    }
}