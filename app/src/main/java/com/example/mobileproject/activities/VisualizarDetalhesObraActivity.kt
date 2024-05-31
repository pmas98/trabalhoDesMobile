package com.example.mobileproject.activities

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileproject.R
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.util.Locale

class VisualizarDetalhesObraActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    var textToSpeech: TextToSpeech? = null
    var buttonPlay: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_detalhes_obra)

        var nomeFontSize = 34f
        var tituloFontSize = 60f
        var descricaoFontSize = 26f

        val intent = Intent(this, UserMenuActivity::class.java)
        val extras = intent.extras;
        if (extras != null) {
            // pega o id da obra lido no qrcode
            val id = extras.getString("id")
            getObraDetails(id, intent)
        }

        findViewById<ImageButton>(R.id.inicioButton)
            .setOnClickListener {
                startActivity(intent)
            }

        findViewById<Button>(R.id.aumetarButton)
            .setOnClickListener {
                // aumentar a fonte dos textos
                if (nomeFontSize != 42f) {
                    nomeFontSize += 4
                    tituloFontSize += 4
                    descricaoFontSize += 4
                    findViewById<TextView>(R.id.nomeAutor).setTextSize(TypedValue.COMPLEX_UNIT_SP, nomeFontSize)
                    findViewById<TextView>(R.id.nomeObra).setTextSize(TypedValue.COMPLEX_UNIT_SP, tituloFontSize)
                    findViewById<TextView>(R.id.descricaoObra).setTextSize(TypedValue.COMPLEX_UNIT_SP, descricaoFontSize)
                }
            }

        findViewById<Button>(R.id.diminuirButton)
            .setOnClickListener {
                // diminuir a fonte dos textos
                if (nomeFontSize != 34f) {
                    nomeFontSize -= 4
                    tituloFontSize -= 4
                    descricaoFontSize -= 4
                    findViewById<TextView>(R.id.nomeAutor).setTextSize(TypedValue.COMPLEX_UNIT_SP, nomeFontSize)
                    findViewById<TextView>(R.id.nomeObra).setTextSize(TypedValue.COMPLEX_UNIT_SP, tituloFontSize)
                    findViewById<TextView>(R.id.descricaoObra).setTextSize(TypedValue.COMPLEX_UNIT_SP, descricaoFontSize)
                }
            }

        buttonPlay = findViewById(R.id.button_play_audio)
        buttonPlay!!.isEnabled = false
        textToSpeech = TextToSpeech(this, this)

        buttonPlay!!.setOnClickListener {
            if (textToSpeech!!.isSpeaking){
                textToSpeech!!.stop()
            } else {
                speakOut()
            }
        }
    }

    private fun getObraDetails(id: String?, intent: Intent) {
        val client = OkHttpClient()

        val url = "https://backendapp-production-da1c.up.railway.app/obraId?id={$id}"
        val request = Request.Builder()
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
                    if (response.code != 200) {
                        Log.d("OkHTTP", "id nao existe")
                        intent.putExtra("erro_id", true)
                        startActivity(intent)
                    } else {
                        // pegar dados
                        val jsonElement = Json.parseToJsonElement(responseBody)

                        if (jsonElement is JsonObject) {
                            var value = jsonElement["autor"]?.jsonPrimitive?.content
                            if (value != null) {
                                runOnUiThread {
                                    findViewById<TextView>(R.id.nomeAutor).text = value
                                }
                            }
                            value = jsonElement["name"]?.jsonPrimitive?.content
                            if (value != null) {
                                runOnUiThread {
                                    findViewById<TextView>(R.id.nomeObra).text = value
                                }
                            }
                            value = jsonElement["description"]?.jsonPrimitive?.content
                            if (value != null) {
                                runOnUiThread {
                                    findViewById<TextView>(R.id.descricaoObra).text = value
                                }
                            }
                        }
                    }
                    Log.d("OkHTTP", responseBody)
                }
            }
        })
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech!!.setLanguage(Locale.getDefault())
            textToSpeech!!.setSpeechRate(0.8F)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language is not supported!")
            } else {
                buttonPlay!!.isEnabled = true
            }
        }
    }

    fun speakOut() {
        val text = findViewById<TextView>(R.id.descricaoObra).text.toString()
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }
}