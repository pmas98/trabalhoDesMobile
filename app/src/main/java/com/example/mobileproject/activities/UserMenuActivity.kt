package com.example.mobileproject.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
import com.example.mobileproject.R
import com.example.mobileproject.activities.ui.theme.MobileProjectTheme
import com.example.mobileproject.databinding.ActivityUserMenuBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


class UserMenuActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        isGranted: Boolean ->
        if (isGranted) {
            showCamera()
        }
    }

    private val scanLauncher = registerForActivityResult(ScanContract()) {
        result: ScanIntentResult -> run {
            if (result.contents == null) {
                Log.d("teste", "nada lido")
            } else {
                Log.d("teste", result.contents)
                val intent = Intent(this, VisualizarDetalhesObraActivity::class.java)
                intent.putExtra("id", result.contents)
                startActivity(intent)
            }
        }
    }

    private lateinit var binding: ActivityUserMenuBinding

    private fun showCamera() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setOrientationLocked(true)

        scanLauncher.launch(options)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initViews()

        findViewById<ImageButton>(R.id.loginButton)
            .setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }
    }

    private fun initViews() {
        binding.buttonScan.setOnClickListener {
            checkPermissionCamera(this)
        }
    }

    private fun checkPermissionCamera(context: Context) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            showCamera()
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            Log.d("teste", "permissao da camera negada")
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun initBinding() {
        binding = ActivityUserMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
