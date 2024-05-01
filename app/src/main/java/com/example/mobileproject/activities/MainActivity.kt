package com.example.mobileproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobileproject.activities.LoginActivity
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela1)

        startActivity(Intent(this, LoginActivity::class.java))
    }
}
