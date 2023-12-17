package com.example.gobatik.injection

import android.content.Context
import com.example.gobatik.repository.CameraRepository
import com.example.gobatik.retrofit.ConfigApi

object Datas {
    fun provideRepository(context: Context): CameraRepository {
        val apiService = ConfigApi.getApiService()
        return CameraRepository(apiService)
    }
}