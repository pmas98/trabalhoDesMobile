import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileproject.R
import android.graphics.Paint



class SelecaoObrasAdapter(private val items: List<String>) : RecyclerView.Adapter<SelecaoObrasAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btn: Button = view.findViewById(R.id.ButtonSelecaoObras)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.selecao_obras_items_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.btn.text = items[position]
        holder.btn.autoResizeText()
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
