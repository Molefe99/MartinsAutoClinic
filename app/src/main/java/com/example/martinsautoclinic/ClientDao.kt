package com.example.martinsautoclinic


import androidx.room.Dao
import androidx.room.Query

@Dao
interface ClientDao {
    @Query("SELECT * FROM clients WHERE id = :clientId LIMIT 1")
    suspend fun getClient(clientId: Int): Client
}
