package com.example.mobileproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela1)

        val client = OkHttpClient()

        val url = "https://backendapp-production-da1c.up.railway.app/expo"

        val request = Request.Builder()
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
