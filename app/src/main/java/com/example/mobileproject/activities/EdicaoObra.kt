package com.example.mobileproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.mobileproject.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive


suspend fun getObraByID(id: String) : String {
    var respostaFinal = "Erro"
    val url = "https://backendapp-production-da1c.up.railway.app/obraId?id=${id}"
    val client = OkHttpClient()

    val request = Request.Builder()
        .url(url)
        .build()

    val response = withContext(Dispatchers.IO) {
        client.newCall(request).execute()
    }

    if (!response.isSuccessful){
        return "Unexpected code ${response}"
//        throw IOException("Unexpected code $response")
    }

    respostaFinal = response.body?.string() ?: "No response"

    return respostaFinal
}

fun obterObraJason(response: String): MutableMap<String, String> {
    val jsonElement = Json.parseToJsonElement(response)
//    val result = mutableListOf<String>()
    var result: MutableMap<String, String> = mutableMapOf()
    if (jsonElement is JsonObject) {
        var value = jsonElement["expoId"]?.jsonPrimitive?.content
        if (value != null) {
            result["expoId"] = value
        }
        value = jsonElement["id"]?.jsonPrimitive?.content
        if (value != null) {
            result["id"] = value
        }
        value = jsonElement["name"]?.jsonPrimitive?.content
        if (value != null) {
            result["name"] = value
        }
        value = jsonElement["autor"]?.jsonPrimitive?.content
        if (value != null) {
            result["autor"] = value
        }
        value = jsonElement["description"]?.jsonPrimitive?.content
        if (value != null) {
            result["description"] = value
        }
        value = jsonElement["imageURL"]?.jsonPrimitive?.content
        if (value != null) {
            result["imageURL"] = value
        }
    }

    return result
}

suspend fun patchObra(obraData: MutableMap<String,String>): String{

    val url = "https://backendapp-production-da1c.up.railway.app/obra"
    Log.d("OkHTTP", url)
    val client = OkHttpClient()
    val mediaType = "application/json; charset=utf-8".toMediaType()
    val requestBody = """
                {
                     "id": "${obraData["id"]}",
                     "name": "${obraData["name"]}",
                     "autor": "${obraData["autor"]}",
                     "description": "${obraData["description"]}",
                     "imageURL": "${obraData["imageURL"]}"
                }
            """.trimIndent().toRequestBody(mediaType)

    val request = Request.Builder()
        .patch(requestBody)
        .url(url)
        .build()

    val response = withContext(Dispatchers.IO) {
        client.newCall(request).execute()
    }

    if (!response.isSuccessful){
        Log.d("MYmobileproject", "Not successul ${response}")
//        throw IOException("Unexpected code $response")
    }

    return response.body?.string() ?: "No response"
}



class EdicaoObra : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edicao_obra)

        val obraID = intent.getStringExtra("id")

        var nomeObraEditText: TextView = findViewById(R.id.input_obra_name)
        var autorObraEditText: TextView = findViewById(R.id.input_obra_autor)
        var descricaoObraEditText: TextView = findViewById(R.id.input_obra_description)

        var atualizarObraBtn: Button = findViewById(R.id.button_atualizar)

        var obraData: MutableMap<String, String> = mutableMapOf()
        runBlocking {
            if (obraID != null){
                val obraJason = getObraByID(obraID)
                obraData = obterObraJason(obraJason)
            }
        }

        Log.d("MYmobileproject", "${obraData}")
        nomeObraEditText.text = obraData["name"].toString()
        autorObraEditText.text = obraData["autor"].toString()
        descricaoObraEditText.text = obraData["description"].toString()

        nomeObraEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Enable the button if there are more than 3 characters in the EditText
                obraData["name"] = nomeObraEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable) {
                Log.d("MYmobileproject", "${obraData}")
            }
        })

        autorObraEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Enable the button if there are more than 3 characters in the EditText
                obraData["autor"] = autorObraEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable) {
                Log.d("MYmobileproject", "${obraData}")
            }
        })

        descricaoObraEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Enable the button if there are more than 3 characters in the EditText
                obraData["description"] = descricaoObraEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable) {
                Log.d("MYmobileproject", "${obraData}")
            }
        })

        atualizarObraBtn.setOnClickListener{
            val activityContex = this@EdicaoObra

            runBlocking {
                val resposta = patchObra(obraData)
                Log.d("MYmobileproject", resposta)
                val intent = Intent(activityContex, SelecaoObras2::class.java)
                startActivity(intent)
            }
        }

    }
}

