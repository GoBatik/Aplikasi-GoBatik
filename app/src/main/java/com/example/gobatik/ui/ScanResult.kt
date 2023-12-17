package com.example.gobatik.ui

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gobatik.R
import com.example.gobatik.databinding.ActivityScanResultBinding
import java.io.File

class ScanResult : AppCompatActivity() {
    private lateinit var binding: ActivityScanResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_scan_result)

        val imageFile = intent.getParcelableExtra<Bitmap>("picture")
        if (imageFile != null){
            binding.image.setImageBitmap(imageFile)
        }
    }
}