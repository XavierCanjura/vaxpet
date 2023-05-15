package com.example.vaxpet.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vaxpet.mascotaData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MascotaViewModel: ViewModel() {
    private  var mascotaLiveData = MutableLiveData<List<mascotaData>>()

    var firebaseDatabase = FirebaseDatabase.getInstance()

    fun getMascotasByPropietario(idPropietario: String, tipo: String? = null){
        firebaseDatabase.getReference("mascotas").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val mascotas = snapshot.children.mapNotNull {
                        it.getValue(mascotaData::class.java)
                    }.toList()

                    var mascotasByPropietario: MutableList<mascotaData> = ArrayList()
                    for (mascota in mascotas){
                        if(tipo != null && mascota.getTipo() != tipo){
                            continue
                        }

                        if(mascota.getIdPropietario() == idPropietario || true){// temporal para probar
                            mascotasByPropietario.add(mascota)
                        }
                    }

                    mascotaLiveData.value = mascotasByPropietario
                }
            }

            override fun onCancelled(error: DatabaseError) { }

        })
    }

    fun observerCartLiveData(): LiveData<List<mascotaData>> {
        return mascotaLiveData
    }
}