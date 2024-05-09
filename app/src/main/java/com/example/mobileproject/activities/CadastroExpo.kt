package com.example.mobileproject.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileproject.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import kotlinx.coroutines.*

class CadastroExpo : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_expo)

        var waitingResponse = true

        var newExpoAdded: Boolean = false
        var newExpoName = " "
        fun changeState(state: Boolean){
            newExpoAdded = state
            Log.d("OkHTTP", "state: ${newExpoAdded} | command: ${state}")
        }

        fun toastGenerator(){
            Log.d("OkHTTP", "StateBeforeToast: ${newExpoAdded.toString()}")
            if (newExpoAdded) {
                var toast = Toast.makeText(this@CadastroExpo, "Exposição cadastrada com sucesso", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            } else {
                var toast = Toast.makeText(this@CadastroExpo, "Erro ao cadastrar exposição", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                toast.show()
            }
        }

        fun fetchTasks() {
            var response_state = false
            val url = "https://backendapp-production-da1c.up.railway.app/expo"
            Log.d("OkHTTP", url)
            val client = OkHttpClient()
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = """
                {
                    "name": "${newExpoName}"
                }
            """.trimIndent().toRequestBody(mediaType)
            Log.d("OkHTTP", newExpoName)

            val request = Request.Builder()
                .post(requestBody)
                .url(url)
                .build()
            Log.d("OkHTTP", request.toString())

            client.newCall(request).enqueue(object: Callback{
                override fun onFailure(call: Call, e: java.io.IOException) {
                    e.printStackTrace()
                    Log.d("OkHTTP", "fail2")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful){
                        changeState(false)
                        throw IOException("Unexpected code $response")
                    }

                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        Log.d("OkHTTP", responseBody)
                        changeState(true)
                        Log.d("OkHTTP", newExpoAdded.toString())
                        newExpoName = " "
                        waitingResponse = false
                    }
                    Log.d("OkHTTP", "reponse_state: ${response_state}")


                }
            })
        }

        var inputNovaExpo: EditText = findViewById(R.id.input_nova_expo)
        var buttonCadastrar: Button = findViewById(R.id.button_cadastrar)
        buttonCadastrar.isEnabled = false


        inputNovaExpo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                buttonCadastrar.isEnabled = s.length > 3
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Enable the button if there are more than 3 characters in the EditText
                buttonCadastrar.isEnabled = s.length > 3
            }

            override fun afterTextChanged(s: Editable) {
                buttonCadastrar.isEnabled = s.length > 3
            }
        })


        buttonCadastrar.setOnClickListener {
            newExpoName = inputNovaExpo.text.toString()
            fetchTasks()
            while (waitingResponse){}
            toastGenerator()
            inputNovaExpo.text.clear()
            waitingResponse = true
        }


    }

}