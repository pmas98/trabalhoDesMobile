package com.example.mobileproject.activities

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileproject.R
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.mobileproject.activities.CadastroExpo
import com.example.mobileproject.activities.SelecaoObras2



class SelecaoObrasAdapter(private val items: List<String>, val nomeObras: List<String>, val idsObras: List<String>, val context: Context) : RecyclerView.Adapter<SelecaoObrasAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null
    private var onClickButon: Click? = null
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun setOnClickButonListener(onClickButon: Click){
        this.onClickButon = onClickButon
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }

    interface Click{
        fun onBtnClick(position: Int)
    }

    inner class ViewHolder(view: View, nomes: List<String> = nomeObras.toList(), ids: List<String> = idsObras.toList(), screenContext: Context = context) : RecyclerView.ViewHolder(view) {
        val btn: Button = view.findViewById(R.id.ButtonSelecaoObras)

        init {
            btn.setOnClickListener {
                Log.d("OkHTTP", btn.text.toString())
                var index = nomes.indexOf(btn.text.toString())
                Log.d("OkHTTP", index.toString())
                Log.d("OkHTTP", ids.toString())
                val id = ids[index]
                Log.d("OkHTTP", id)
                var intent = Intent(screenContext, CadastroExpo::class.java)
                startActivity(screenContext, intent, null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.selecao_obras_items_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.btn.text = items[position]
        holder.btn.autoResizeText()
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position)
            }
        }
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

            while ((textWidth < buttonWidth/3)) {
                textSize += 1f
                paint.textSize = textSize
                textWidth = paint.measureText(buttonText)
                if (textSize > 28f) {
                    break
                }
            }

            this.textSize = textSize
        }
    }
}
