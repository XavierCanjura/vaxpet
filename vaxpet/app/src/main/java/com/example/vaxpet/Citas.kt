package com.example.vaxpet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vaxpet.adapters.CitaAdapter
import com.example.vaxpet.databinding.FragmentCitasBinding
import com.example.vaxpet.databinding.FragmentHomeBinding
import com.example.vaxpet.models.CitasModel
import com.example.vaxpet.viewmodels.CitaViewModel
import com.google.firebase.auth.FirebaseAuth

class Citas : Fragment() {
    private var _binding: FragmentCitasBinding? = null
    private val binding get() = _binding!!

    private var idPropietario: String = ""

    var citas: MutableList<CitasModel> = ArrayList()
    private var citaAdapter: CitaAdapter? = null
    private lateinit var viewModel: CitaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCitasBinding.inflate(inflater, container, false)
        val view = binding.root

        initReclycerView()
        var auth = FirebaseAuth.getInstance()
        idPropietario = auth.currentUser!!.uid

        viewModel = ViewModelProvider(this)[CitaViewModel::class.java]
        viewModel.getCitasByPropietario(idPropietario)
        viewModel.observerCartLiveData().observe(viewLifecycleOwner, Observer { citaList ->
            citaAdapter!!.setCitaList(citaList)
        })

        return view
    }

    private fun initReclycerView(){
        citaAdapter = CitaAdapter(citas)
        binding.rvListadoCitas.layoutManager = LinearLayoutManager(context)
        binding.rvListadoCitas.adapter = citaAdapter
    }
}