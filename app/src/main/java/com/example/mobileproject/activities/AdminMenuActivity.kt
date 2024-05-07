package com.example.mobileproject.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileproject.R

class AdminMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_menu)

        findViewById<Button>(R.id.button_obras_cadastradas)
            .setOnClickListener {
                // colocar nome da activity de cadastrar exposição
                // startActivity(Intent(this, CadastrarExpoActivity::class.java))
            }

        findViewById<Button>(R.id.button_cadastrar_expo)
            .setOnClickListener {
                // colocar nome da activity da tela de obras
                // startActivity(Intent(this, ObrasCadastradasActivity::class.java))
            }
    }
}