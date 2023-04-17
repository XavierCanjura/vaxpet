package com.example.vaxpet.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.vaxpet.databinding.MascotaItemBinding
import com.example.vaxpet.mascotaData

class MascotaViewHolder(view: View): ViewHolder(view) {
    private val binding = MascotaItemBinding.bind(view)

    fun render(mascota: mascotaData, onClickListener: (mascotaData) -> Unit){
        binding.tvNombreMascota.text = mascota.getNombre()
        binding.tvEdad.text = mascota.getFechaNacimiento()
        binding.tvRaza.text = mascota.getRaza()
        Glide.with(binding.ivFoto.context).load(mascota.getPath()).into(binding.ivFoto)
        binding.cardMascota.setOnClickListener{ onClickListener(mascota) }
    }
}