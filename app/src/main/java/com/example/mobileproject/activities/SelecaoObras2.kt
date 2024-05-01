package com.example.mobileproject.activities

import SelecaoObrasAdapter

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import com.example.mobileproject.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun getObraById(id: String): String {
    val client = OkHttpClient()

    val request = Request.Builder()
        .url("https://backendapp-production-da1c.up.railway.app/obra")
        .build()

    val response = withContext(Dispatchers.IO) {
        client.newCall(request).execute()
    }

    if (!response.isSuccessful) throw IOException("Unexpected code $response")

    return response.body?.string() ?: "No response"
}

fun obterNomesObras(response: String): List<String> {
    val jsonElement = Json.parseToJsonElement(response)
    val result = mutableListOf<String>()

    if (jsonElement is JsonArray) {
        for (item in jsonElement) {
            if (item is JsonObject) {
                val name = item["name"]?.jsonPrimitive?.content
                if (name != null) {
                    result.add(name)
                }
            }
        }
    }

    return result
}
fun foo() : List<String> {
    var resposta = " "
    runBlocking {

        resposta = getObraById("R4V0Cmd6MFGqFLsZR50O")
        Log.d("OkHTTP", "--->${resposta}")

    }

    var listaNomes: List<String> = obterNomesObras(resposta)

    return listaNomes
}

class SelecaoObras2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecao_obras2)

        var items = List(5) { "Item $it" } // Replace with your actual data

        items = foo()

        val recyclerView: RecyclerView = findViewById(R.id.RecyclerViewSelecaoObras)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SelecaoObrasAdapter(items)

    }
}