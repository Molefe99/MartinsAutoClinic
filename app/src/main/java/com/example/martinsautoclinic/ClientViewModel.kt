package com.example.martinsautoclinic


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ClientViewModel(private val repository: ClientRepository) : ViewModel() {

    private val _client = MutableLiveData<Client>()
    val client: LiveData<Client> get() = _client

    fun fetchClient(clientId: Int) {
        viewModelScope.launch {
            val clientData = repository.getClient(clientId)
            _client.postValue(clientData)
        }
    }
}
