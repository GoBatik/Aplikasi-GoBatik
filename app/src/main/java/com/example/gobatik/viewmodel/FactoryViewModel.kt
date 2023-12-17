package com.example.gobatik.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gobatik.injection.Datas
import com.example.gobatik.repository.CameraRepository

class FactoryViewModel (
    private val repository: CameraRepository
) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CameraViewModel::class.java) -> {
                CameraViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: FactoryViewModel? = null
        fun getInstance(context: Context): FactoryViewModel =
            instance ?: synchronized(this) {
                instance ?: FactoryViewModel(Datas.provideRepository(context))
            }.also {
                instance = it
            }
    }

}