package com.example.mobileproject.activities
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
class AtalhosAPI {
    fun verExpos() {
        val url = "https://backendapp-production-da1c.up.railway.app/expo"
        Log.d("OkHTTP", url)
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()
        Log.d("OkHTTP", request.toString())

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                e.printStackTrace()
                Log.d("OkHTTP", "fail2")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful){
                    throw IOException("Unexpected code $response")
                }

                val responseBody = response.body?.string()
                if (responseBody != null) {
                    Log.d("OkHTTP", responseBody)
                }
            }
        })
    }
    fun apagarExpo(id: String) {
        val url = "https://backendapp-production-da1c.up.railway.app/expo"
        Log.d("OkHTTP", url)
        val client = OkHttpClient()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = """
                {
                    "id": "${id}"
                }
            """.trimIndent().toRequestBody(mediaType)

        val request = Request.Builder()
            .delete(requestBody)
            .url(url)
            .build()
        Log.d("OkHTTP", request.toString())

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                e.printStackTrace()
                Log.d("OkHTTP", "fail2")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful){
                    throw IOException("Unexpected code $response")
                }

                val responseBody = response.body?.string()
                if (responseBody != null) {
                    Log.d("OkHTTP", responseBody)
                }
            }
        })
    }

    suspend fun addObra(id: String, name: String, autor: String, description: String, imageURL: String ) {
        val url = "https://backendapp-production-da1c.up.railway.app/obra"
        Log.d("OkHTTP", url)
        val client = OkHttpClient()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = """
                {
                    "id": "${id}",
                    "name": "${name}",
                    "autor": "${autor}",
                    "description": "${description}",
                    "imageURL": "${imageURL}"
                }
            """.trimIndent().toRequestBody(mediaType)

        val request = Request.Builder()
            .post(requestBody)
            .url(url)
            .build()
        Log.d("OkHTTP", request.toString())

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                e.printStackTrace()
                Log.d("OkHTTP", "fail2")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful){
                    throw IOException("Unexpected code $response")
                }

                val responseBody = response.body?.string()
                if (responseBody != null) {
                    Log.d("OkHTTP", responseBody)
                }
            }
        })
    }
}