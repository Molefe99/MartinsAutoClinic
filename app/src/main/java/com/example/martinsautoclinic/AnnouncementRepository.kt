package com.example.martinsautoclinic


import android.net.Uri
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AnnouncementRepository {

    private val database = FirebaseDatabase.getInstance().getReference("announcements")
    private val storage = FirebaseStorage.getInstance().reference

    fun saveAnnouncement(title: String, imageUri: Uri, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val announcementId = database.push().key ?: return onFailure("Failed to generate ID")
        val storageRef = storage.child("announcement_images/$announcementId.jpg")

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val announcementData = mapOf(
                        "title" to title,
                        "imageUrl" to uri.toString(),
                        "timestamp" to System.currentTimeMillis()
                    )
                    database.child(announcementId).setValue(announcementData)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onSuccess()
                            } else {
                                onFailure(task.exception?.message ?: "Unknown error")
                            }
                        }
                }.addOnFailureListener {
                    onFailure("Failed to upload image")
                }
            }
            .addOnFailureListener {
                onFailure("Failed to upload image")
            }
    }
}
