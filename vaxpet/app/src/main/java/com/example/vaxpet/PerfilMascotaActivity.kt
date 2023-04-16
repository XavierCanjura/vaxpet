package com.example.vaxpet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vaxpet.databinding.ActivityPerfilMascotaBinding

class PerfilMascotaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilMascotaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilMascotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.atras.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}