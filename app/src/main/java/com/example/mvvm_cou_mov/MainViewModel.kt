package com.example.mvvm_cou_mov

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*


class MainViewModel constructor(private val mainRepository: MainRepository) : ViewModel() {

        val errorMessage = MutableLiveData<String>()
        val movieList = MutableLiveData<List<Movie>>()
        var job: Job? = null

        val loading = MutableLiveData<Boolean>()

        fun getAllMovies() {
            job = CoroutineScope(Dispatchers.IO).launch {
                val response = mainRepository.getAllMovies()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        movieList.postValue(response.body())
                        loading.value = false
                    } else {
                        onError("Error : ${response.message()} ")
                    }
                }
            }

        }

        private fun onError(message: String) {
            errorMessage.value = message
            loading.value = false
        }

        override fun onCleared() {
            super.onCleared()
            job?.cancel()
        }

    }
