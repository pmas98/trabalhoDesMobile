package com.example.mobileproject.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
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

suspend fun getAllExpos() : String {
    var respostaFinal = "Erro: Sem exposições"
    val url = "https://backendapp-production-da1c.up.railway.app/expo"
    Log.d("OkHTTP", url)
    val client = OkHttpClient()

    val request = Request.Builder()
        .url(url)
        .build()
    Log.d("OkHTTP", request.toString())

    val response = withContext(Dispatchers.IO) {
        client.newCall(request).execute()
    }

    if (!response.isSuccessful){
        return "Unexpected code ${response}"
//        throw IOException("Unexpected code $response")
    }

    respostaFinal = response.body?.string() ?: "No response"

    Log.d("OkHTTP", "--->${respostaFinal}")
    return respostaFinal
}

suspend fun getAllExpoObras(id: String): String {
    var respostaFinal = "Sem Resposta"

    val client = OkHttpClient()
    val url = "https://backendapp-production-da1c.up.railway.app/expoObras?expoId=${id}"

    Log.d("OkHTTP", url)

    val request = Request.Builder()
        .url(url)
        .build()
    Log.d("OkHTTP", request.toString())

    val response = withContext(Dispatchers.IO) {
        client.newCall(request).execute()
    }

    if (!response.isSuccessful){
        return "Unexpected code ${response}"
//        throw IOException("Unexpected code $response")
    }

    respostaFinal = response.body?.string() ?: "No response"

    Log.d("OkHTTP", "All Expo Obras --->${respostaFinal}")


    return respostaFinal
}

suspend fun deleteExpo(expoId: String): String {

    var respostaFinal = "Sem Resposta"

    val client = OkHttpClient()
    val url = "https://backendapp-production-da1c.up.railway.app/expo"

    val mediaType = "application/json; charset=utf-8".toMediaType()
    val requestBody = """
                {
                    "id": "${expoId}"
                }
            """.trimIndent().toRequestBody(mediaType)

    val request = Request.Builder()
        .delete(requestBody)
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

    Log.d("MYmobileproject", "Delete Expo --->${respostaFinal}")

    return respostaFinal
}




fun obterNomesJason(response: String): List<String> {
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

fun obterIdsJason(response: String): List<String> {
    val jsonElement = Json.parseToJsonElement(response)
    val result = mutableListOf<String>()

    if (jsonElement is JsonArray) {
        for (item in jsonElement) {
            if (item is JsonObject) {
                val name = item["id"]?.jsonPrimitive?.content
                if (name != null) {
                    result.add(name)
                }
            }
        }
    }

    return result
}


class SelecaoObras2 : AppCompatActivity() {
    var toRefresh2 = false
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecao_obras2)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val toRefresh: Boolean? = result.data?.getBooleanExtra("refresh", false)
                if (toRefresh == true) {
                    recreate()
                }
            }
        }

        var jasonExpos: String
        var idsExpos: List<String>
        var nomesExpos: List<String>

        runBlocking {

            jasonExpos = getAllExpos()
            idsExpos = obterIdsJason(jasonExpos)
            nomesExpos = obterNomesJason(jasonExpos)
            Log.d("OkHTTP", idsExpos[0])
        }

        var spinner: Spinner = findViewById(R.id.spinnerSelecaoExpo)
        val spinnerItems = nomesExpos
        val adapter = ArrayAdapter(this, R.layout.expo_list_picker, spinnerItems)
        spinner.adapter = adapter

        var jasonObras: String
        var idsObras: List<String>
        var nomesObras: List<String>

        runBlocking {

            jasonObras = getAllExpoObras(idsExpos[0])
            idsObras = obterIdsJason(jasonObras)
            nomesObras = obterNomesJason(jasonObras)

        }

        var recycleViewItems = nomesObras


        val recyclerView: RecyclerView = findViewById(R.id.RecyclerViewSelecaoObras)
        recyclerView.layoutManager = LinearLayoutManager(this)
        var recyclerAdapter = SelecaoObrasAdapter(recycleViewItems, nomesObras, idsObras, this@SelecaoObras2)
        recyclerView.adapter = recyclerAdapter


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Perform your action here
                val index = nomesExpos.indexOf(selectedItem)
                val newId = idsExpos[index]

                runBlocking {

                    jasonObras = getAllExpoObras(newId)
                    idsObras = obterIdsJason(jasonObras)
                    nomesObras = obterNomesJason(jasonObras)
                    toRefresh2 = true
                }

                recycleViewItems = nomesObras
                recyclerView.layoutManager = LinearLayoutManager(this@SelecaoObras2)
                recyclerView.adapter = SelecaoObrasAdapter(recycleViewItems, nomesObras, idsObras,this@SelecaoObras2)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
                Log.d("OkHTTP", "nothing")
            }
        }

        var btnApagarExpo: ImageButton = findViewById(R.id.buttonRemoveExpo)

        btnApagarExpo.setOnClickListener{

            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setView(inflater.inflate(R.layout.delete_pop_up, null))
            val dialog = builder.create()

            dialog.show()

            val yesButton = dialog.findViewById<Button>(R.id.button__confirma_apagar)
            yesButton?.setOnClickListener {
                val selectedItem: String = spinner.selectedItem.toString()
                val index = nomesExpos.indexOf(selectedItem)
                val newId = idsExpos[index]

                Log.d("MYmobileproject", "Remove: $newId")

                runBlocking {

                    deleteExpo(newId)

                }

                val intent = intent
                finish()
                startActivity(intent)
            }

            val noButton = dialog.findViewById<Button>(R.id.button_cancelar)
            noButton?.setOnClickListener {
                dialog.dismiss()
            }

        }

        var buttonAdionar: Button = findViewById(R.id.button_adicionar)

        buttonAdionar.setOnClickListener {

            Log.d("MYmobileproject", "Click Adicionar")
            val selectedItem: String = spinner.selectedItem.toString()
            val index = nomesExpos.indexOf(selectedItem)
            val newId = idsExpos[index]

            val intent = Intent(this, AdicionarObra::class.java)
            intent.putExtra("expoId", newId)
            resultLauncher.launch(intent)
//            startActivity(intent)

        }

    }

    override fun onResume() {
        super.onResume()

        if (toRefresh2){
            val intent = intent
            finish()
            startActivity(intent)
        }
    }
}