package com.example.vaxpet.viewmodels

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

    fun getMascotasByPropietario(idPropietario: String){
        firebaseDatabase.getReference("mascotas").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val mascotas = snapshot.children.mapNotNull {
                        it.getValue(mascotaData::class.java)
                    }.toList()

                    var mascotasByPropietario: MutableList<mascotaData> = ArrayList()
                    for (mascota in mascotas){
                        if(mascota.getIdPropietario() == idPropietario){
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