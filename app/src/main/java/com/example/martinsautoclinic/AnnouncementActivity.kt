package com.example.martinsautoclinic


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AnnouncementActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null

    private lateinit var ivSelectedImagePreview: ImageView
    private lateinit var imgAnnouncement: ImageView
    private lateinit var btnPickImage: Button
    private lateinit var btnPostAnnouncement: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcement) // Replace with your layout file

        // Initialize views
        ivSelectedImagePreview = findViewById(R.id.ivSelectedImagePreview)
        btnPickImage = findViewById(R.id.btnPickImage)
        btnPostAnnouncement = findViewById(R.id.btnPostAnnouncement)
        val backButton: Button = findViewById(R.id.back_button)

        // Check for storage permission at startup
        checkStoragePermission()

        // Set up the image picker
        btnPickImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                // Open the gallery to pick an image
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "image/*"
                }
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
            } else {
                // Request permission if not granted
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    100
                )
            }
        }

        // Set up the post announcement button
        btnPostAnnouncement.setOnClickListener {
            if (selectedImageUri != null) {
                // Post the selected image to the announcements
                imgAnnouncement.setImageURI(selectedImageUri)
                ivSelectedImagePreview.visibility = View.GONE // Hide the preview after posting
                Toast.makeText(this, "Announcement posted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please select an image!", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle Back Button Click
        backButton.setOnClickListener {
            val intent = Intent(this, AdminDashboard::class.java)
            startActivity(intent)
            finish() // Optional: closes the current activity
        }
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                100
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            ivSelectedImagePreview.apply {
                visibility = View.VISIBLE // Show the preview
                setImageURI(selectedImageUri) // Display the selected image
            }
        }
    }
}
