package com.example.mobileproject.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileproject.R
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavArgs
import com.example.mobileproject.activities.CadastroExpo
import com.example.mobileproject.activities.SelecaoObras2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import com.example.mobileproject.activities.AtalhosAPI


class SelecaoObrasAdapter(private val items: List<String>, val nomeObras: List<String>, val idsObras: List<String>, val context: Context) : RecyclerView.Adapter<SelecaoObrasAdapter.ViewHolder>() {

    inner class ViewHolder(
        view: View,
        nomes: List<String> = nomeObras.toList(),
        ids: List<String> = idsObras.toList(),
        screenContext: Context = context
    ) : RecyclerView.ViewHolder(view) {
        val btn: Button = view.findViewById(R.id.ButtonSelecaoObras)
        val btn_edit: Button = view.findViewById(R.id.buttonEditObra)
        val btn_qr: ImageButton = view.findViewById(R.id.buttonQRObra)

        init {

            var api = AtalhosAPI()

            btn_edit.setOnClickListener {
                Log.d("OkHTTP", btn.text.toString())
                var index = nomes.indexOf(btn.text.toString())
                Log.d("OkHTTP", index.toString())
                Log.d("OkHTTP", ids.toString())
                val id = ids[index]
                Log.d("OkHTTP", id)
                var intent = Intent(screenContext, EdicaoObra::class.java)
                intent.putExtra("id", id)
                startActivity(screenContext, intent, null)
            }

            btn_qr.setOnClickListener {

                var index = nomes.indexOf(btn.text.toString())
                val id = ids[index]
                api.downloadQR(id, context)

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.selecao_obras_items_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.btn_edit.visibility = View.INVISIBLE
        holder.btn_qr.visibility = View.INVISIBLE
        holder.btn.visibility = View.INVISIBLE
        holder.btn.text = items[position]


        runBlocking {
            holder.btn.autoResizeText()
            holder.btn_edit.autoResizeText()
        }

        holder.btn.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (holder.btn.visibility == View.VISIBLE) {
                    holder.btn.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    holder.btn_qr.visibility = View.VISIBLE

                }
            }
        })

    }

    override fun getItemCount() = items.size

    fun Button.autoResizeText() {
        this.post {

            val buttonText = this.text.toString()
            val buttonWidth = this.width

            var textSize = 1f
            this.textSize = textSize

            val paint = this.paint
            var textWidth = paint.measureText(buttonText)

            while ((textWidth < buttonWidth / 4)) {
                textSize += 1f
                paint.textSize = textSize
                textWidth = paint.measureText(buttonText)
                if (textSize > 26f) {
                    break
                }
            }

            this.textSize = textSize
            this.visibility = View.VISIBLE
        }

    }

}

