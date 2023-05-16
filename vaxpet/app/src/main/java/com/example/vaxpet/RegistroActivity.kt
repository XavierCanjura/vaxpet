package com.example.vaxpet

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.desafiopractico2.usuarioData
import com.example.vaxpet.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.net.URI

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var ImageUri: Uri
    private lateinit var FirebaseImageUri: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        var firebaseDatabase = FirebaseDatabase.getInstance()
        var firebaseReference = firebaseDatabase.getReference()

        binding.cancelar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.selecionarImagen.setOnClickListener {
            launchGallery()
        }

        binding.registrar.setOnClickListener {
            val nombres = binding.nombres.text.toString();
            val apellidos = binding.apellidos.text.toString();
            val telefono = binding.telefono.text.toString()
            val correo = binding.correo.text.toString()
            val contraseña01 = binding.contrasena01.text.toString()
            val contraseña02 = binding.contrasena02.text.toString()
            ImageUri = Uri.parse("")
            //para objeto de la database
            var usuarioData: usuarioData
            usuarioData = usuarioData()

            fun validarDatos(): Boolean {
                var esValido: Boolean = true;

                if (nombres.isEmpty()) {
                    binding.nombres.setError("Este campo es requerido")
                    esValido = false
                }
                if (apellidos.isEmpty()) {
                    binding.apellidos.setError("Este campo es requerido")
                    esValido = false
                }
                if (telefono.isEmpty()) {
                    binding.telefono.setError("Este campo es requerido")
                    esValido = false
                }
                if (contraseña01.length < 6) {
                    binding.contrasena01.setError("Este campo requiere un minimo de 6 caracteres")
                    esValido = false
                }
                if (contraseña02.length < 6 || !(contraseña02 == contraseña01)) {
                    binding.contrasena02.setError("Ambas contraseñas deben coincidir")
                    esValido = false
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    binding.correo.setError("El formato no coincide con la forma xxx@proveedordeemail.com")
                    esValido = false
                }
                return esValido;
            }

            if (validarDatos()) {
                //aca creo objeto para guardar en la database
                usuarioData.setnombres(nombres)
                usuarioData.setapellidos(apellidos)
                usuarioData.settelefono(telefono)
                usuarioData.setcontraseña(contraseña01)
                usuarioData.setcorreo(correo)


                //aca hago creacion del usuario de autenticacion
                firebaseAuth.createUserWithEmailAndPassword(correo, contraseña01)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {

                            //uploadIamge()
                            usuarioData.setid(it.getResult().user?.uid.toString()) //guardo uid del usuariorecien creado
                            firebaseReference.child("usuarios").push().setValue(usuarioData)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Toast.makeText(
                                            this,
                                            "Usuario Registrado, por favor inicia sesion",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "No se pudo registrar el usuario, por favor intenta mas tarde",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }

                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this,
                                "No se pudo crear la autenticacion el usuario, por favor intenta mas tarde" + it.exception.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }

    private fun uploadIamge() {
        Log.d("ver aca---->", ImageUri.toString())
        Log.d("ver aca---->", Uri.EMPTY.equals(ImageUri).toString())
        if (!Uri.EMPTY.equals(ImageUri)) {
            val random = java.util.Random()
            val fileName = "image00-" + random.nextInt().toString()
            val storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName)
            storageReference.putFile(ImageUri).addOnSuccessListener {

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

            ImageUri = data?.data!!
            binding.imageView.setImageURI(ImageUri)

            if (!Uri.EMPTY.equals(ImageUri)) {

                val random = java.util.Random()
                val fileName = "imageProfile00-" + random.nextInt().toString()
                val storageReference =
                    FirebaseStorage.getInstance().getReference("images/" + fileName)
                storageReference.putFile(ImageUri).addOnSuccessListener { task ->
                    if (task.task.isSuccessful) {
                        val downloadUri = task.task.result
                        FirebaseImageUri = downloadUri.uploadSessionUri.toString()
                    } else {

                    }
                }
            }
        }
    }

}