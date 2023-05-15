package com.example.vaxpet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vaxpet.adapters.MascotaAdapter
import com.example.vaxpet.databinding.FragmentHomeBinding
import com.example.vaxpet.viewmodels.MascotaViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Home : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var idPropietario: String = ""

    var mascotas: MutableList<mascotaData> = ArrayList()
    private var mascotaAdapter: MascotaAdapter? = null
    private lateinit var viewModel: MascotaViewModel

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

        initRecyclerView()
        var auth = FirebaseAuth.getInstance()
        idPropietario = auth.currentUser?.uid.toString()

        getMascotas(idPropietario, null) // this

        binding.fab.setOnClickListener {
            val intent = Intent(context, MantMascotaActivity::class.java)
            intent.putExtra("idPropietario", idPropietario)
            intent.putExtra("isCreate", true)
            startActivity(intent)
        }

        binding.tabPerro.setOnClickListener {
            getMascotas(idPropietario, "Perro")
        }

        binding.tabGato.setOnClickListener {
            getMascotas(idPropietario, "Gato")
        }

        binding.tabTodo.setOnClickListener {
            getMascotas(idPropietario, null)
        }

        // Inflate the layout for this fragment
        return view
    }

    private fun getMascotas(idPropietario: String, tipo: String?) {
        viewModel = ViewModelProvider(this)[MascotaViewModel::class.java]
        viewModel.getMascotasByPropietario(idPropietario, tipo)
        viewModel.observerCartLiveData().observe(viewLifecycleOwner, Observer { mascotaList ->
            mascotaAdapter?.setMascotaList(mascotaList)
        })

        binding.tabTodo.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray))
        binding.tabPerro.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray))
        binding.tabGato.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray))

        if(tipo == "Perro") {
            binding.tabPerro.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.pink
                )
            )
        } else if(tipo == "Gato") {
            binding.tabGato.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.pink
                )
            )
        } else {
            binding.tabTodo.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.pink
                )
            )
        }

    }

    private fun initRecyclerView() {
        mascotaAdapter = MascotaAdapter(mascotas) { mascota ->
            onItemSelected(mascota)
        }
        binding.rvListadoMascotas.layoutManager = GridLayoutManager(context, 2)
        binding.rvListadoMascotas.adapter = mascotaAdapter
    }

    private fun onItemSelected(mascota: mascotaData) {
        val intent = Intent(context, PerfilMascotaActivity::class.java)
        intent.putExtra("idMascota", mascota.getId())
        startActivity(intent)
    }
}
