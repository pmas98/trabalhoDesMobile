package com.example.mobileproject.activities
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.mobileproject.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import java.io.File
import java.io.FileOutputStream

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

    fun downloadQR(obraId: String, context: Context) {

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

                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                val responseBody = response.body?.bytes() // Get the bytes of the response body

                if (responseBody != null) {
                    resolver.openOutputStream(uri!!)?.use { outputStream ->
                        outputStream.write(responseBody)
                    }
                }
            }
        })
    }

}