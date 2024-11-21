package com.example.martinsautoclinic


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey val id: String,
    val title: String,
    val imageUrl: String,
    val timestamp: Long
)
