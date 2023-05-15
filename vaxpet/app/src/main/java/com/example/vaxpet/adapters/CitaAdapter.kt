package com.example.vaxpet.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.vaxpet.R
import com.example.vaxpet.models.CitasModel

class CitaAdapter(private  var citaList: List<CitasModel>): Adapter<CitaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CitaViewHolder(layoutInflater.inflate(R.layout.citas_item, parent, false))
    }

    override fun getItemCount(): Int = citaList.size

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = citaList[position]
        holder.render(cita)
    }

    fun setCitaList(newCitaList: List<CitasModel>){
        this.citaList = newCitaList
        notifyDataSetChanged()
    }
}