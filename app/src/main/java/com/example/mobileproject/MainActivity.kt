package com.example.mobileproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mobileproject.activities.CadastroExpo
import com.example.mobileproject.activities.SelecaoObras2

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val butaoTeste: Button = findViewById(R.id.button_test)
        butaoTeste.setOnClickListener {
            val intent = Intent(this, SelecaoObras2::class.java)
            startActivity(intent)
        }
    }
}