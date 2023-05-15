package com.example.vaxpet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.desafiopractico2.usuarioData
import com.example.vaxpet.databinding.ActivityEditarPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditarPerfil : AppCompatActivity() {


    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserID: String
    private lateinit var btnEditarCuentaAtras: Button
    private lateinit var txtNombres: EditText
    private lateinit var txtApellidos: EditText
    private lateinit var txtTelefono: EditText
    private lateinit var txtCorreoElectronico: EditText
    private lateinit var txtContrasena: EditText
    private lateinit var txtConfirmarContrasena: EditText
    private lateinit var btnGuardarEditarUsuario: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        // Inicializar la instancia de Firebase Auth y obtener el ID del usuario actual
        auth = FirebaseAuth.getInstance()
        currentUserID = auth.currentUser!!.uid

        // Inicializar la instancia de Firebase Database
        database = FirebaseDatabase.getInstance().getReference()

        //Mapeando los elementos
        btnEditarCuentaAtras = findViewById<Button>(R.id.btnEditarCuentaAtras)
        txtNombres = findViewById<EditText>(R.id.txtNombres)
        txtApellidos = findViewById<EditText>(R.id.txtApellidos)
        txtTelefono = findViewById<EditText>(R.id.txtTelefono)
        txtCorreoElectronico = findViewById<EditText>(R.id.txtCorreoElectronico)
        txtContrasena = findViewById<EditText>(R.id.txtContrasena)
        txtConfirmarContrasena = findViewById<EditText>(R.id.txtConfirmarContrasena)
        btnGuardarEditarUsuario = findViewById<Button>(R.id.btnGuardarEditarUsuario)


        //Listener para regresar
        btnEditarCuentaAtras.setOnClickListener {
            finish()
        }


        btnGuardarEditarUsuario.setOnClickListener {


            database.child("usuarios").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {


                    for (userSnapshot in snapshot.children) {

                        val user = userSnapshot.getValue(usuarioData::class.java)

                        if (currentUserID == user?.getid()) {
                            var usuarioData: usuarioData
                            usuarioData = usuarioData()

                            fun validarDatos(): Boolean {
                                var esValido: Boolean = true;

                                if (txtNombres.text.toString().isEmpty()) {
                                    txtNombres.setError("Este campo es requerido")
                                    esValido = false
                                }
                                if (txtApellidos.text.toString().isEmpty()) {
                                    txtApellidos.setError("Este campo es requerido")
                                    esValido = false
                                }
                                if (txtTelefono.text.toString().isEmpty()) {
                                    txtTelefono.setError("Este campo es requerido")
                                    esValido = false
                                }
                                if (txtContrasena.text.toString().length < 6) {
                                    txtContrasena.setError("Este campo requiere un minimo de 6 caracteres")
                                    esValido = false
                                }
                                if (txtContrasena.text.toString().length < 6 || txtContrasena.text.toString() != txtConfirmarContrasena.text.toString()) {
                                    txtConfirmarContrasena.setError("Ambas contraseñas deben coincidir")
                                    esValido = false
                                }
                                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(txtCorreoElectronico.text.toString()).matches()) {
                                    txtCorreoElectronico.setError("El formato no coincide con la forma xxx@proveedordeemail.com")
                                    esValido = false
                                }
                                return esValido;
                            }

                            if (validarDatos()){
                                //Creando objeto a guardar en firebase
                                usuarioData.setnombres(txtNombres.text.toString())
                                usuarioData.setapellidos(txtApellidos.text.toString())
                                usuarioData.settelefono(txtTelefono.text.toString())
                                usuarioData.setcontraseña(txtContrasena.text.toString())
                                usuarioData.setcorreo(txtCorreoElectronico.text.toString())
                                usuarioData.setid(user?.getid())

                                //Instancias para manejar al usuario actual
                                val mAuth = FirebaseAuth.getInstance()
                                val user = mAuth.currentUser

                                //Modificando tanto el correo como la contraseña de Authentication
                                user?.updateEmail(txtCorreoElectronico.text.toString())?.addOnCompleteListener {
                                    task ->
                                    if (task.isSuccessful){
                                        user?.updatePassword(txtContrasena.text.toString())?.addOnCompleteListener {
                                            task2 ->
                                            if (task2.isSuccessful){

                                                //Guardando en firebase
                                                database.child("usuarios").child(userSnapshot.key.toString()).setValue(usuarioData)
                                                Toast.makeText(
                                                    this@EditarPerfil,
                                                    "Cambios guardados",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                finish()
                                            }
                                        }
                                    }
                                    else{
                                        Toast.makeText(
                                            this@EditarPerfil,
                                            "Error al guardar los datos",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }



                            }

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })


        }


        //Recorriendo los nodos para mostrar los datos en pantalla
        database.child("usuarios").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                for (userSnapshot in snapshot.children) {

                    val user = userSnapshot.getValue(usuarioData::class.java)

                    if (currentUserID == user?.getid()) {
                        txtNombres.setText(user?.getnombres())
                        txtApellidos.setText(user?.getapellidos())
                        txtTelefono.setText(user?.gettelefono())
                        txtCorreoElectronico.setText(user?.getcorreo())

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }



}