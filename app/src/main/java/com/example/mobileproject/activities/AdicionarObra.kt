package com.example.mobileproject.activities



import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import com.example.mobileproject.R
import kotlinx.coroutines.runBlocking



class AdicionarObra : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_obra)

        var apiManager = AtalhosAPI()

        val expoId = intent.getStringExtra("expoId")

        var nomeObraEditText: TextView = findViewById(R.id.input_obra_name)
        var autorObraEditText: TextView = findViewById(R.id.input_obra_autor)
        var descricaoObraEditText: TextView = findViewById(R.id.input_obra_description)

        var adicionarObraBtn: Button = findViewById(R.id.button_adicionar)

        var obraData: MutableMap<String, String> = mutableMapOf("expoId" to expoId!!.toString(),
            "name" to " ", "autor" to " ", "description" to " ", "imageURL" to "TODO")
        Log.d("MYmobileproject", "${obraData}")

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

        descricaoObraEditText.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                v.parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }

        descricaoObraEditText.setOnFocusChangeListener { v, hasFocus ->
            // Show cursor// Remove cursor
            descricaoObraEditText.isCursorVisible = hasFocus
        }

        val mainScrollView: ScrollView = findViewById(R.id.mainScrollView)

        mainScrollView.setOnTouchListener{ v, event ->

            var isUserEditingText: Boolean = nomeObraEditText.isFocused or
                    autorObraEditText.isFocused or
                    descricaoObraEditText.isFocused

            if ( isUserEditingText ) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                nomeObraEditText.clearFocus()
                autorObraEditText.clearFocus()
                descricaoObraEditText.clearFocus()
            }
            false
        }

        adicionarObraBtn.setOnClickListener{
            val activityContex = this@AdicionarObra

            runBlocking {

                val resposta = apiManager.addObra(obraData)
                Log.d("MYmobileproject", resposta)
                finish()
            }

        }

    }
}