package com.example.vaxpet

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.desafiopractico2.usuarioData
import com.example.vaxpet.databinding.ActivityMantMascotaBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class MantMascotaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMantMascotaBinding
    private lateinit var firebaseDatabase: FirebaseDatabase

    private var id: String = "1681626882272"
    private var idPropietario: String = ""
    private var nombre: String = ""
    private var raza: String = ""
    private var sexo: String = ""
    private var fecha_nacimiento: String = ""
    private var descripcion: String = ""
    private var imagen: String = ""
    private lateinit var imageUri: Uri

    private var isCreate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMantMascotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idPropietario = intent.getStringExtra("idPropietario").toString()
        isCreate = intent.getBooleanExtra("isCreate", true)

        firebaseDatabase = FirebaseDatabase.getInstance()

        if (isCreate) {
            binding.btRegistrar.setText(R.string.guardar)
        } else {
            binding.btRegistrar.setText(R.string.actualizar)
            obtenerMascota()

            binding.etNombreMascota.setText(nombre)
        }

        // Cuando se da click en registrar
        binding.btRegistrar.setOnClickListener {
            nombre = binding.etNombreMascota.text.toString()
            raza = binding.etRaza.text.toString()
            sexo = binding.etSexo.text.toString()
            fecha_nacimiento = binding.etFechaNacimiento.text.toString()
            descripcion = binding.etDescripcion.text.toString()

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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }

        // Cuando se da click en seleccionar imagen
        binding.ibSelecionarImagen.setOnClickListener {
            launchGallery()
        }


    }

    private fun isValidPet(): Boolean {
        var isValid: Boolean = true

        if (nombre.isEmpty()) {
            binding.etNombreMascota.setError("Ingrese el nombre de la mascota")
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
                            Glide.with(this@MantMascotaActivity)
                                .load("https://firebasestorage.googleapis.com/v0/b/vaxpet-5ff55.appspot.com/o/images%2Fpet%2Fpet00--1569130601?alt=media")
                                .into(binding.ivMascotaImage)
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
        mascotaData.setIdPropietario("HRm1DvtEODVFwwuL6ffSr5LfR9b2")
        mascotaData.setNombre(nombre)
        mascotaData.setRaza(raza)
        mascotaData.setSexo(sexo)
        mascotaData.setFechaNacimiento(fecha_nacimiento)
        mascotaData.setDescripcion(descripcion)
        mascotaData.setImagen(imagen)

        firebaseDatabase.getReference().child("mascotas").push().setValue(mascotaData)
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
        firebaseDatabase.getReference().child("mascotas").child("")
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