package com.example.mobileproject.activities
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.mobileproject.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okio.IOException
import java.io.File
import java.io.FileOutputStream


val REQUEST_CODE_DIRECTORY = 200

class AtalhosAPI() {
    fun verExpos() {
        val url = "https://backendapp-production-da1c.up.railway.app/expo"
        Log.d("OkHTTP", url)
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()
        Log.d("OkHTTP", request.toString())

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                e.printStackTrace()
                Log.d("OkHTTP", "fail2")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                }

                val responseBody = response.body?.string()
                if (responseBody != null) {
                    Log.d("OkHTTP", responseBody)
                }
            }
        })
    }

    fun apagarExpo(id: String) {
        val url = "https://backendapp-production-da1c.up.railway.app/expo"
        Log.d("OkHTTP", url)
        val client = OkHttpClient()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = """
                {
                    "id": "${id}"
                }
            """.trimIndent().toRequestBody(mediaType)

        val request = Request.Builder()
            .delete(requestBody)
            .url(url)
            .build()
        Log.d("OkHTTP", request.toString())

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                e.printStackTrace()
                Log.d("OkHTTP", "fail2")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                }

                val responseBody = response.body?.string()
                if (responseBody != null) {
                    Log.d("OkHTTP", responseBody)
                }
            }
        })
    }

    suspend fun addObra(
        id: String,
        name: String,
        autor: String,
        description: String,
        imageURL: String
    ) {
        val url = "https://backendapp-production-da1c.up.railway.app/obra"
        Log.d("OkHTTP", url)
        val client = OkHttpClient()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = """
                {
                    "id": "${id}",
                    "name": "${name}",
                    "autor": "${autor}",
                    "description": "${description}",
                    "imageURL": "${imageURL}"
                }
            """.trimIndent().toRequestBody(mediaType)

        val request = Request.Builder()
            .post(requestBody)
            .url(url)
            .build()
        Log.d("OkHTTP", request.toString())

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                e.printStackTrace()
                Log.d("OkHTTP", "fail2")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                }

                val responseBody = response.body?.string()
                if (responseBody != null) {
                    Log.d("OkHTTP", responseBody)
                }
            }
        })
    }

    suspend fun addObra(obraData: MutableMap<String, String>): String {

        val url = "https://backendapp-production-da1c.up.railway.app/obra"
        Log.d("OkHTTP", url)
        val client = OkHttpClient()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestString = """
                {
                     "expoId": "${obraData["expoId"]}",
                     "name": "${obraData["name"]}",
                     "autor": "${obraData["autor"]}",
                     "description": "${obraData["description"]}",
                     "imageURL": "${obraData["imageURL"]}"
                }
            """

        Log.d("MYmobileproject", requestString)

        val requestBody = requestString.trimIndent().toRequestBody(mediaType)

        val request = Request.Builder()
            .post(requestBody)
            .url(url)
            .build()

        val response = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }

        if (!response.isSuccessful) {
            Log.d("MYmobileproject", "Not successul ${response}")
//        throw IOException("Unexpected code $response")
        }

        return response.body?.string() ?: "No response"

    }

    suspend fun deleteObra(obraId: String): String {

        var respostaFinal = "Sem Resposta"

        val client = OkHttpClient()
        val url = "https://backendapp-production-da1c.up.railway.app/obra"

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = """
                {
                    "id": "${obraId}"
                }
            """.trimIndent().toRequestBody(mediaType)

        val request = Request.Builder()
            .delete(requestBody)
            .url(url)
            .build()


        val response = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }

        if (!response.isSuccessful) {
            return "Unexpected code ${response}"
//        throw IOException("Unexpected code $response")
        }

        respostaFinal = response.body?.string() ?: "No response"

        Log.d("MYmobileproject", "Delete Expo --->${respostaFinal}")

        return respostaFinal

    }

    fun showSnackbarQR(view: View, message: String) {
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
        val customView = LayoutInflater.from(view.context).inflate(R.layout.toast_qr, null)
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)

        val text: TextView = customView.findViewById(R.id.qr_toast_message)
        text.text = message

        snackbarLayout.addView(customView, 0)
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)
        snackbar.show()
    }

    fun downloadQR(obraId: String, context: Context, view: View) {

        val client = OkHttpClient()

        val url = "https://backendapp-production-da1c.up.railway.app/qrcode?id=${obraId}"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                // Handle failure
                e.printStackTrace()
                Log.d("OkHTTP", "sem resposta")
            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.d("OkHTTP", "QR: erro no ciente")
                    return
                }

                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(
                        MediaStore.MediaColumns.DISPLAY_NAME,
                        "qr_${obraId}.png"
                    ) // The name of the file
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png") // The MIME type of the file
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_DOWNLOADS
                    ) // The relative path to the file within the external storage
                }


                // Use the selected directory URI to save the file

                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                val responseBody = response.body?.bytes() // Get the bytes of the response body

                if (responseBody != null) {
                    resolver.openOutputStream(uri!!)?.use { outputStream ->
                        outputStream.write(responseBody)
                    }
                }

                (context as Activity).runOnUiThread {
                    showSnackbarQR(view, "QR Code salvo em Downloads")
                }

            }
        })
    }

    fun saveAudioFile(context: Context, audioBytes: ByteArray, displayName: String, mimeType: String) {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val audioUri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)

        resolver.openOutputStream(audioUri!!)?.use { outputStream ->
            outputStream.write(audioBytes)
        }
    }

    fun getBinaryString(context: Context, fileUri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(fileUri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        return bytes?.joinToString(separator = "") { String.format("%8s", Integer.toBinaryString(it.toInt() and 0xFF)).replace(' ', '0') } ?: ""
    }


    fun convertFileToBase64(context: Context, fileUri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(fileUri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
    fun patchObra(obraData: MutableMap<String,String>, context: Context) {

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

        Log.d("OkHTTP", obraData.toString())
        Log.d("OkHTTP", """
            {
                 "id": "${obraData["id"]}",
                 "name": "${obraData["name"]}",
                 "autor": "${obraData["autor"]}",
                 "description": "${obraData["description"]}",
                 "imageURL": "${obraData["imageURL"]}"
            }
        """.trimIndent())

        val request = Request.Builder()
            .patch(requestBody)
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                // Handle failure
                e.printStackTrace()
                Log.d("OkHTTP", "sem resposta")
            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.d("OkHTTP", "QR: erro no cliente")
                    Log.d("OkHTTP", response.toString())
                    return
                }

                Log.d("MYmobileproject", response.toString())
                (context as Activity).finish()
            }
        })

    }

}