package com.example.vaxpet

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.vaxpet.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        //Inicio de sesion con Google
        //Desplegando la opcion para iniciar sesion con Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)

        binding.sesionGoogle.setOnClickListener {
            signInGoogle()
        }

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
                        binding.correoElectronico.setText("")
                        binding.editTexContraseA.setText("")

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

    private fun signInGoogle() {
        val sighInIntent = googleSignInClient.signInIntent
        launcher.launch(sighInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResult(task)
        }
    }

    //Manejo de resultado si ya se tiene una cuenta de Google asociada
    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }
        }
        else{
            Toast.makeText(this,task.exception.toString(),Toast.LENGTH_LONG).show()
        }
    }

    //Funcion que realiza el inicio de sesion y manda a llamar a la MainActivity a mostrar
    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                val intent : Intent = Intent(this,MainActivity::class.java)
                intent.putExtra("name",account.displayName)
                startActivity(intent)
            }
            else{
                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_LONG).show()
            }
        }
    }
}