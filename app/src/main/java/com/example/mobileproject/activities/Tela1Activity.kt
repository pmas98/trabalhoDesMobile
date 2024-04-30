package com.example.mobileproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobileproject.R
import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException

class Tela1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela1)

        val url = "https://backendapp-production-da1c.up.railway.app/"
        Log.d("pojeto_web", url)
        fun fetchTasks() {
            val client = OkHttpClient()
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = """
                {
                    "email": "filipets@edu.unifor.br",
                    "password": "12345678"
                }
            """.trimIndent().toRequestBody(mediaType)


            val request = Request.Builder()
                .post(requestBody)
                .url("${url}signup")
                .build()
            Log.d("pojeto_web", request.toString())

            client.newCall(request).enqueue(object: Callback{
                override fun onFailure(call: Call, e: java.io.IOException) {
                    e.printStackTrace()
                    Log.d("pojeto_web", "fail2")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val anserw = response.body.toString()
                    Log.d("pojeto_web", anserw)
                }
            })
        }

        fetchTasks()


    }
}