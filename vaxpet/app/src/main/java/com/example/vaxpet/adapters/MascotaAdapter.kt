package com.example.vaxpet.adapters

import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.vaxpet.R
import com.example.vaxpet.mascotaData

class MascotaAdapter(private var mascotaList: List<mascotaData>, private var onClickListener: (mascotaData) -> Unit): Adapter<MascotaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MascotaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MascotaViewHolder(layoutInflater.inflate(R.layout.mascota_item, parent, false))
    }

    override fun onBindViewHolder(holder: MascotaViewHolder, position: Int) {
        val mascota = mascotaList[position]
        holder.render(mascota, onClickListener)
    }

    override fun getItemCount(): Int = mascotaList.size

    fun setMascotaList(newMascotaList: List<mascotaData>){
        this.mascotaList = newMascotaList
        notifyDataSetChanged()
    }
}