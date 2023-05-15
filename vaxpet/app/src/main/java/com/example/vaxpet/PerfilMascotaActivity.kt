package com.example.vaxpet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.vaxpet.databinding.ActivityPerfilMascotaBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PerfilMascotaActivity : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var binding: ActivityPerfilMascotaBinding

    private var idMascota: String = ""
    private var fotoMascota: String = ""
    private var edad: String = ""
    private var raza: String = ""
    private var descripcion = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilMascotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()

        idMascota = intent.getStringExtra("idMascota").toString()
        getMascotaById()

        binding.btnEditar.setOnClickListener {
            val intent = Intent(this, MantMascotaActivity::class.java)
            intent.putExtra("idMascota", idMascota)
            intent.putExtra("isCreate", false)
            startActivity(intent)
        }

        binding.atras.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.citas.setOnClickListener {
            val intent = Intent(this, AddCitaActivity::class.java)
            intent.putExtra("idMascota", idMascota)
            startActivity(intent)
        }
    }

    fun getMascotaById(){
        firebaseDatabase.getReference("mascotas").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val mascotas = snapshot.children.mapNotNull {
                        it.getValue(mascotaData::class.java)
                    }.toList()

                    for (data in mascotas)
                    {
                        if(data.getId() == idMascota){
                            binding.tvNombre.text = data.getNombre()
                            binding.tvDescripcion.text = data.getDescripcion()
                            binding.tvRaza.text = data.getRaza()

                            Glide.with(this@PerfilMascotaActivity)
                                .load(data.getPath())
                                .into(binding.ivFotoMascota)

                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error", error.message)
            }
        })
    }
}