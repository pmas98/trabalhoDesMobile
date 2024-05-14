package com.example.mobileproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.mobileproject.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

         findViewById<Button>(R.id.button_login)
             .setOnClickListener {
                 val email = findViewById<EditText>(R.id.input_email).text.toString()
                 val password = findViewById<EditText>(R.id.input_password).text.toString()
                 val intent = Intent(this, AdminMenuActivity::class.java)

                 loginRequest(email, password, intent)
             }
    }
    fun loginRequest(email: String, password: String, intent: Intent) {
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
                Log.d("OkHTTP", "sem resposta")
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle response
                val responseBody = response.body?.string()

                if (responseBody != null) {
                    if (response.code == 400) {
                        Log.d("OkHTTP", "e-mail ou usuario invalidos")

                        if (email == "") {
                            runOnUiThread {
                                findViewById<EditText>(R.id.input_email).error = "Insira um e-mail"
                                findViewById<EditText>(R.id.input_email).setText("")
                            }
                        }

                        if (password == "") {
                            runOnUiThread {
                                findViewById<EditText>(R.id.input_password).error = "Insira uma senha"
                                findViewById<EditText>(R.id.input_password).setText("")
                            }
                        }

                        if (email != "" && password != ""){
                            runOnUiThread {
                                findViewById<EditText>(R.id.input_password).error = ""
                                findViewById<EditText>(R.id.input_password).setText("")
                                findViewById<EditText>(R.id.input_email).error = ""
                                findViewById<EditText>(R.id.input_email).setText("")
                                findViewById<TextView>(R.id.error).visibility = View.VISIBLE
                            }
                        }

                    } else if (response.code == 200) {
                        // call next activity
                        startActivity(intent)
                    }
                    Log.d("OkHTTP", responseBody)
                }
                println(responseBody)
            }
        })
    }
}