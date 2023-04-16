package com.example.vaxpet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vaxpet.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton



class Home : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var idPropietario: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.fab.setOnClickListener {
            val intent = Intent(context, MantMascotaActivity::class.java)
            intent.putExtra("idPropietario", idPropietario)
            intent.putExtra("isCreate", true)
            startActivity(intent)
        }

        // Inflate the layout for this fragment
        return view
    }
}
