package com.example.martinsautoclinic


class ClientRepository(private val clientDao: ClientDao) {

    suspend fun getClient(clientId: Int): Client {
        return clientDao.getClient(clientId)
    }
}
