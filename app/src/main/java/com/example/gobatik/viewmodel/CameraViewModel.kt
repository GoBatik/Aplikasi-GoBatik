package com.example.gobatik.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gobatik.repository.CameraRepository
import okhttp3.MultipartBody

class CameraViewModel (
    private val repository: CameraRepository
) : ViewModel() {

    fun postImage(
        file: MultipartBody.Part
    ) = repository.postImage(file)
}