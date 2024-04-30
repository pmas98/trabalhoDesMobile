package com.example.mobileproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.mobileproject.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class Tela1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela1)

         findViewById<Button>(R.id.button_login)
             .setOnClickListener {
                 val email = findViewById<EditText>(R.id.input_email).text.toString()
                 val password = findViewById<EditText>(R.id.input_password).text.toString()

                 loginRequest(email, password)
             }
    }
    fun loginRequest(email: String, password: String) {
        val client = OkHttpClient()

        val url = "https://backendapp-production-da1c.up.railway.app/login"

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = """
            {
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent().toRequestBody(mediaType)

        val request = Request.Builder()
            .post(requestBody)
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle response
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    Log.d("OkHTTP", responseBody)
                }
                println(responseBody)
            }
        })
    }
}