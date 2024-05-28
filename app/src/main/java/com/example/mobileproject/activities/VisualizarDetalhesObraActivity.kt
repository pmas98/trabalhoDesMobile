package com.example.mobileproject.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileproject.R

class VisualizarDetalhesObraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_detalhes_obra)

        var extras = intent.extras;
        if (extras != null) {
            // pega o id da obra lido no qrcode
            val id = extras.getString("id")
        }

        findViewById<ImageButton>(R.id.inicioButton)
            .setOnClickListener {
                startActivity(Intent(this, UserMenuActivity::class.java))
            }

        findViewById<Button>(R.id.button_play_audio)
            .setOnClickListener {
                // tocar audio
            }

        findViewById<Button>(R.id.aumetarButton)
            .setOnClickListener {
                // aumentar a fonte dos textos (34sp -> 39sp -> 44sp; 22sp -> 26sp -> 30sp)
            }

        findViewById<Button>(R.id.diminuirButton)
            .setOnClickListener {
                // diminuir a fonte dos textos (44sp -> 39sp -> 34sp; 30sp -> 26sp -> 22sp)
            }
    }
}