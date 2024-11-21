package com.example.martinsautoclinic


import android.net.Uri
import androidx.lifecycle.ViewModel

class AnnouncementViewModel : ViewModel() {

    private val repository = AnnouncementRepository()

    fun saveAnnouncementToDatabase(
        title: String,
        imageUri: Uri,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        repository.saveAnnouncement(title, imageUri, onSuccess, onFailure)
    }
}
