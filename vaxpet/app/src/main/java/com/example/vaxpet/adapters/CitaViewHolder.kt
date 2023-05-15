package com.example.vaxpet.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.vaxpet.databinding.CitasItemBinding
import com.example.vaxpet.models.CitasModel

class CitaViewHolder(view: View): ViewHolder(view) {
    private  val binding = CitasItemBinding.bind(view)

    fun render(cita: CitasModel){
        binding.chkBanio.isChecked = cita.getBanio()!!
        binding.chkCorte.isChecked = cita.getCorte()!!
        binding.tvFecha.text = cita.getFecha()
        binding.tvHora.text = cita.getHora()
    }
}