package com.example.mobileproject.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.text.toSpannable
import androidx.lifecycle.withStarted
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Locale


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


class EdicaoObra : AppCompatActivity(), TextToSpeech.OnInitListener {
    var playBtn: Button? = null
    var textToSpeech: TextToSpeech? = null
    var descricaoObraEditText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edicao_obra)

        var apiManager = AtalhosAPI()

        val obraID = intent.getStringExtra("id")

        var nomeObraEditText: TextView = findViewById(R.id.input_obra_name)
        var autorObraEditText: TextView = findViewById(R.id.input_obra_autor)
        descricaoObraEditText = findViewById(R.id.input_obra_description)



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
        descricaoObraEditText!!.text = obraData["description"]


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

        nomeObraEditText.setOnFocusChangeListener { v, hasFocus ->
            // Show cursor// Remove cursor
            nomeObraEditText.isCursorVisible = hasFocus
        }

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

        autorObraEditText.setOnFocusChangeListener { v, hasFocus ->
            // Show cursor// Remove cursor
            autorObraEditText.isCursorVisible = hasFocus
        }

        descricaoObraEditText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Enable the button if there are more than 3 characters in the EditText
                obraData["description"] = descricaoObraEditText!!.text.toString()
            }

            override fun afterTextChanged(s: Editable) {
                Log.d("MYmobileproject", "${obraData}")
            }
        })

        descricaoObraEditText!!.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                v.parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }

        descricaoObraEditText!!.setOnFocusChangeListener { v, hasFocus ->
            // Show cursor// Remove cursor
            descricaoObraEditText!!.isCursorVisible = hasFocus
        }

        val mainScrollView: ScrollView = findViewById(R.id.mainScrollView)

        mainScrollView.setOnTouchListener{ v, event ->

            var isUserEditingText: Boolean = nomeObraEditText.isFocused or
                    autorObraEditText.isFocused or
                    descricaoObraEditText!!.isFocused

            if ( isUserEditingText ) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                nomeObraEditText.clearFocus()
                autorObraEditText.clearFocus()
                descricaoObraEditText!!.clearFocus()
            }
            false
        }

        var atualizarObraBtn: Button = findViewById(R.id.button_atualizar)

        atualizarObraBtn.setOnClickListener{
            val activityContex = this@EdicaoObra

//            runBlocking {
//                val resposta = patchObra(obraData)
//                Log.d("MYmobileproject", resposta)
//                finish()
//            }
            Log.d("MYmobileproject", obraData.toString())
            apiManager.patchObra(obraData,this@EdicaoObra)
        }

        var apagarObraBtn: Button = findViewById(R.id.button_apagar)

        apagarObraBtn.setOnClickListener {
            val activityContex = this@EdicaoObra
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setView(inflater.inflate(R.layout.delete_pop_up, null))
            val dialog = builder.create()

            dialog.show()

            val yesButton = dialog.findViewById<Button>(R.id.button__confirma_apagar)
            yesButton?.setOnClickListener {

                runBlocking {

                    apiManager.deleteObra(obraID.toString())
                    finish()

                }
            }

            val noButton = dialog.findViewById<Button>(R.id.button_cancelar)
            noButton?.setOnClickListener {
                dialog.dismiss()
            }

        }

        var adicionarAudioBtn: Button = findViewById(R.id.button_adicionar_audio)
        var audioFilePath: String = ""

        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                audioFilePath = uri.path.toString()
                val audioString = apiManager.convertFileToBase64(this@EdicaoObra, uri)
                obraData["imageURL"] = audioFilePath
            }

            Log.d("TesteMP3", obraData["imageURL"]!!)
        }

        adicionarAudioBtn.setOnClickListener {

            getContent.launch("audio/mpeg")

        }

        playBtn= findViewById(R.id.button_play_audio)
        playBtn!!.isEnabled = false
        textToSpeech = TextToSpeech(this, this)

        playBtn!!.setOnClickListener {
            if (textToSpeech!!.isSpeaking){
                textToSpeech!!.stop()
            } else {
                speakOut()
            }
        }

    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech!!.setLanguage(Locale.getDefault())
            textToSpeech!!.setSpeechRate(0.8F)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language not supported!")
            } else {
                playBtn!!.isEnabled = true
            }
        }
    }

    fun speakOut() {
        val text = descricaoObraEditText!!.text.toString()
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

}




