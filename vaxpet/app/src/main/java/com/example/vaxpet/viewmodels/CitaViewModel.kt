package com.example.vaxpet.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vaxpet.mascotaData
import com.example.vaxpet.models.CitasModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CitaViewModel: ViewModel() {
    private var citaLiveData = MutableLiveData<List<CitasModel>>()

    var firebaseDatabase = FirebaseDatabase.getInstance()

    fun getCitasByPropietario(idPropietario: String){
        firebaseDatabase.getReference("citas").addValueEventListener( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val citas = snapshot.children.mapNotNull {
                        it.getValue(CitasModel::class.java)
                    }.toList()

                    var citasByPropietario: MutableList<CitasModel> = ArrayList()
                    for (cita in citas){
                        if(cita.getIdPropietario() == idPropietario){
                            citasByPropietario.add(cita)
                        }
                    }

                    citaLiveData.value = citasByPropietario
                }
            }

            override fun onCancelled(error: DatabaseError) { }

        })
    }

    fun observerCartLiveData(): LiveData<List<CitasModel>> {
        return citaLiveData
    }
}