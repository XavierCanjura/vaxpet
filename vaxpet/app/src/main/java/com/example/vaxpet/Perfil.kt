package com.example.vaxpet


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.desafiopractico2.usuarioData
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*




class Perfil : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserID: String
    private lateinit var txtUsuario: TextView
    private lateinit var imgUsuario: ShapeableImageView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)
        txtUsuario = view.findViewById<View>(R.id.txtUsuario) as TextView
        imgUsuario = view.findViewById<View>(R.id.imgUsuario) as ShapeableImageView


        // Inicializar la instancia de Firebase Auth y obtener el ID del usuario actual
        auth = FirebaseAuth.getInstance()
        currentUserID = auth.currentUser!!.uid

        // Inicializar la instancia de Firebase Database
        database = FirebaseDatabase.getInstance().getReference()

        //Recorriendo los nodos
        database.child("usuarios").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val sb = StringBuilder()

                for (userSnapshot in snapshot.children) {

                    val user = userSnapshot.getValue(usuarioData::class.java)

                    if (currentUserID == user?.getid()){
                        val nombres = user?.getnombres()
                        val apellidos = user?.getapellidos()
                        sb.append("$nombres")
                        sb.append(" ")
                        sb.append("$apellidos")

                        txtUsuario.text = sb.toString()
                        val btnEditarCuenta = view.findViewById<View>(R.id.btnEditarCuenta) as Button
                        btnEditarCuenta.isVisible = true
                        break
                    }
                    else{
                        txtUsuario.text = auth.currentUser!!.displayName
                        val btnEditarCuenta = view.findViewById<View>(R.id.btnEditarCuenta) as Button
                        btnEditarCuenta.isVisible = false

                    }
                }
        }

            override fun onCancelled(error: DatabaseError) {

            }
        })



        //Obtiene referencia de botón.
        val btnEditarCuenta = view.findViewById<View>(R.id.btnEditarCuenta) as Button
        val btnCerrarSesion = view.findViewById<View>(R.id.btnCerrarSesion) as Button

        //Asigna listener para poder abrir Activity.
        btnEditarCuenta.setOnClickListener{ view: View ->
            val intent = Intent (activity , EditarPerfil::class.java)
            activity?.startActivity(intent)
        }

        //Asigna listener para cerrar sesion.
        btnCerrarSesion.setOnClickListener{ view: View ->

            auth.signOut().also {

                getActivity()?.onBackPressed()
                activity?.finish()
                val intent = Intent (activity , LoginActivity::class.java)
                activity?.startActivity(intent)


            }
        }

        // Inflate the layout for this fragment
        return view
    }

}