package com.example.vaxpet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.vaxpet.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.crearCuenta.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        binding.iniciarSession.setOnClickListener {
            //obtengo los campos
            val email = binding.correoElectronico.text.toString();
            val pass = binding.editTexContraseA.text.toString();

            fun validarDatos(): Boolean {
                var esValido: Boolean = true;

                if (email.isEmpty()) {
                    binding.correoElectronico.setError("Este campo es requerido")
                    esValido = false
                }
                if (pass.isEmpty()) {
                    binding.editTexContraseA.setError("Este campo es requerido")
                    esValido = false
                }
                if (pass.length < 6) {
                    binding.editTexContraseA.setError("Este campo requiere un minimo de 6 caracteres")
                    esValido = false
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.correoElectronico.setError("El formato no coincide con la forma xxx@proveedordeemail.com")
                    esValido = false
                }
                return esValido;
            }

            if (validarDatos()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this,
                            "Ha ocurrido un problema, por favor verifica que tu usuario y contraseña sean los correctos",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

    }
}