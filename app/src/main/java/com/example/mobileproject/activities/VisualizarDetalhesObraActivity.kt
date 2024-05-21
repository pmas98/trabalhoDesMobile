package com.example.mobileproject.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileproject.R

class VisualizarDetalhesObraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_detalhes_obra)

        findViewById<ImageButton>(R.id.inicioButton)
            .setOnClickListener {
                startActivity(Intent(this, UserMenuActivity::class.java))
            }

        findViewById<ImageButton>(R.id.button_play_audio)
            .setOnClickListener {
                // tocar audio
            }


    }
}