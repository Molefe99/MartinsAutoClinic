package com.example.martinsautoclinic


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ClientViewModelFactory(private val repository: ClientRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClientViewModel::class.java)) {
            return ClientViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
