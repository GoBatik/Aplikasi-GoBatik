package com.example.gobatik.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.gobatik.Results
import com.example.gobatik.databinding.ActivityMainBinding
import com.example.gobatik.utils.reduceFileImage
import com.example.gobatik.utils.rotateBitmap
import com.example.gobatik.utils.showToast
import com.example.gobatik.viewmodel.CameraViewModel
import com.example.gobatik.viewmodel.FactoryViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MainActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var factory: FactoryViewModel
    private val viewModel: CameraViewModel by viewModels {
        factory
    }
    private var getFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = FactoryViewModel.getInstance(this)
        binding.back.visibility = View.GONE
        binding.cardView.visibility = View.GONE
        binding.tvRegister.visibility = View.GONE
        binding.btnStore.visibility = View.GONE
        binding.topImage2.visibility = View.GONE

        setAction()

        if (!permissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    private fun setAction() {
        binding.btnCamera.setOnClickListener {
            startCameraX()
        }
        binding.btnStore.setOnClickListener {

        }
        binding.back.setOnClickListener{
            startCameraX()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun postImage(image: File) {
        Log.d("postimage", "masuk")
        val file = reduceFileImage(image)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile
        )

        viewModel.postImage(imageMultipart).observe(this) { response ->
            when (response) {
                is Results.Success -> {
                    binding.txtNamaRumah.text = "Nama Batik: " + response.data.data.batik_name
                    binding.txtBatikDesc.text = response.data.data.batik_desc
                    binding.progressBar.visibility = View.GONE
                }
                is Results.Error -> {
                    showToast(response.error)
                }
                is Results.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun permissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CAMERA_X_RESULT) {
            val intent = Intent(this, MainActivity::class.java)
            val mFile = result.data?.getSerializableExtra("picture") as File
            val isBackCamera = result.data?.getBooleanExtra("isBackCamera", true) as Boolean
            Log.d("isBack", "before postImage")
            postImage(mFile)
            getFile = mFile
            val resultBitmap = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )

            binding.addImage.setImageBitmap(resultBitmap)
            binding.btnCamera.visibility = View.GONE
            binding.textView2.visibility = View.GONE
            binding.topImage.visibility = View.GONE
            binding.bottomImage.visibility = View.GONE
            binding.cardView.visibility = View.VISIBLE
            binding.tvRegister.visibility = View.VISIBLE
            binding.back.visibility = View.VISIBLE
            binding.btnStore.visibility = View.VISIBLE
            binding.topImage2.visibility = View.VISIBLE
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finishAffinity()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 100
    }
}