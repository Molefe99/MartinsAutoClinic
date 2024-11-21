package com.example.martinsautoclinic


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider

class AnnouncementActivity : AppCompatActivity() {

    private lateinit var viewModel: AnnouncementViewModel
    private lateinit var ivSelectedImagePreview: ImageView
    private lateinit var btnPickImage: Button
    private lateinit var btnPostAnnouncement: Button
    private lateinit var titleEditText: EditText
    private lateinit var backButton: Button
    private var selectedImageUri: Uri? = null

    // Register for image selection result using ActivityResultContracts
    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri
                ivSelectedImagePreview.visibility = View.VISIBLE
                ivSelectedImagePreview.setImageURI(uri)
                Toast.makeText(this, "Image selected!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcement)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(AnnouncementViewModel::class.java)

        // Initialize views
        ivSelectedImagePreview = findViewById(R.id.ivSelectedImagePreview)
        btnPickImage = findViewById(R.id.uploadButton)
        btnPostAnnouncement = findViewById(R.id.saveButton)
        titleEditText = findViewById(R.id.titleEditText)
        backButton = findViewById(R.id.back_button)

        // Check storage permissions at startup
        checkStoragePermission()

        // Set up image picker button click listener
        btnPickImage.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        // Set up save announcement button click listener
        btnPostAnnouncement.setOnClickListener {
            val title = titleEditText.text.toString()

            // Ensure that both the title and the selected image are provided
            if (title.isEmpty() || selectedImageUri == null) {
                Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show()
            } else {
                // Call ViewModel to save the announcement to Firebase
                viewModel.saveAnnouncementToDatabase(
                    title,
                    selectedImageUri!!,
                    onSuccess = {
                        Toast.makeText(this, "Announcement saved!", Toast.LENGTH_SHORT).show()
                        finish() // Close activity
                    },
                    onFailure = { error ->
                        Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        // Handle back button click
        backButton.setOnClickListener {
            val intent = Intent(this, AdminDashboard::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Check if the app has the necessary storage permission
    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                100
            )
        }
    }
}
