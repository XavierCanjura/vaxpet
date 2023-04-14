package com.example.vaxpet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.vaxpet.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.cancelar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.registrar.setOnClickListener {
            val nombres = binding.nombres.text.toString();
            val apellidos = binding.apellidos.text.toString();
            val telefono = binding.telefono.text.toString()
            val correo = binding.correo.text.toString()
            val contraseña01 = binding.contrasena01.text.toString()
            val contraseña02 = binding.contrasena02.text.toString()

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
                //aca hago creacion del usuario de autenticacion
                firebaseAuth.createUserWithEmailAndPassword(correo, contraseña01)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Usuario Registrado, por favor inicia sesion",
                                Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this,
                                "No se pudo registrare el usuario, por favor intenta mas tarde",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }
}