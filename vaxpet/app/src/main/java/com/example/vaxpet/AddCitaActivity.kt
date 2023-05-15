package com.example.vaxpet

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.vaxpet.databinding.ActivityAddCitaBinding
import com.example.vaxpet.models.CitasModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class AddCitaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCitaBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    private var ckBanio: Boolean = false
    private var ckCorte: Boolean = false
    private var etHora: String = ""
    private var etFecha: String = ""
    private var idMascota: String = ""
    private var idPropietario: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idMascota = intent.getStringExtra("idMascota").toString()

        firebaseDatabase = FirebaseDatabase.getInstance()

        auth = FirebaseAuth.getInstance()
        idPropietario = auth.currentUser!!.uid

        getMascotaById()

        binding.etDate.setOnClickListener { showDatePickerDialog() }

        binding.registrar.setOnClickListener {
            ckBanio = binding.chkBanio.isChecked
            ckCorte = binding.chkCorte.isChecked
            etHora = binding.etTime.text.toString()
            etFecha = binding.etDate.text.toString()

            if (isValidCita()) {
                createCita()
            }
        }

        binding.cancelar.setOnClickListener {
            val intent = Intent(this, PerfilMascotaActivity::class.java)
            intent.putExtra("idMascota", idMascota)
            startActivity(intent)
            finish()
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = AddCitaActivity.DatePickerFragment { day, month, year ->
            onDateSelected(
                day,
                month,
                year
            )
        }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun onDateSelected(day: Int, month: Int, year: Int) {
        val monthNow = month+1
        binding.etDate.setText("$day/$monthNow/$year")
    }

    fun isValidCita(): Boolean {
        var isValid = true

        if (!ckCorte && !ckBanio) {
            Toast.makeText(this, "Selecciona algun tipo de cita", Toast.LENGTH_LONG).show()
            isValid = false
        }
        if (etFecha.trim().isEmpty()) {
            binding.etDate.setError("Ingrese la fecha de la cita")
            isValid = false
        }
        if (etHora.trim().isEmpty()) {
            binding.etTime.setError("Ingrese la hora de la cita")
        }

        return isValid
    }

    private fun createCita() {
        var citasModel: CitasModel
        citasModel = CitasModel()

        citasModel.setId(System.currentTimeMillis().toString())
        citasModel.setIdMascota(idMascota)
        citasModel.setBanio(ckBanio)
        citasModel.setCorte(ckCorte)
        citasModel.setHora(etHora)
        citasModel.setFecha(etFecha)
        citasModel.setIdPropietario(idPropietario)

        firebaseDatabase.getReference().child("citas").child(citasModel.getId().toString())
            .setValue(citasModel)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "La cita fue creada correctamente", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Ocurrion un problema al registrar la cita, intentelo mas tarde",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun getMascotaById(){
        firebaseDatabase.getReference("mascotas").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val mascotas = snapshot.children.mapNotNull {
                        it.getValue(mascotaData::class.java)
                    }.toList()

                    for (data in mascotas)
                    {
                        if(data.getId() == idMascota){
                            binding.tvMascotaName.text = data.getNombre()

                            Glide.with(this@AddCitaActivity)
                                .load(data.getPath())
                                .into(binding.ivFotoMascotascota)

                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error", error.message)
            }
        })
    }

    class DatePickerFragment(val listener: (day: Int, month: Int, year: Int) -> Unit) :
        DialogFragment(), DatePickerDialog.OnDateSetListener {

        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            listener(dayOfMonth, month, year)
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()
            val day = c.get(Calendar.DAY_OF_MONTH)
            val month = c.get(Calendar.MONTH)
            val year = c.get(Calendar.YEAR)

            val picker = DatePickerDialog(activity as Context, this, year, month, day)
            picker.datePicker.minDate = c.timeInMillis
            return picker
        }

    }
}