package com.example.vaxpet.views

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.vaxpet.MainActivity
import com.example.vaxpet.R

class SuccessActivity : AppCompatActivity() {

    private var titulo: String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        titulo = intent.getStringExtra("titulo").toString()

        var tvTitulo = findViewById<TextView>(R.id.tvTitulo)
        var btnRegresar = findViewById<Button>(R.id.btnRegresar)

        tvTitulo.text = titulo

        btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}