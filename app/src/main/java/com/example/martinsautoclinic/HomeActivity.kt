package com.example.martinsautoclinic

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class HomeActivity : AppCompatActivity() {

    private lateinit var imgAnnouncement: ImageView;
    private lateinit var tvNoAnnouncements: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // Initialize views
        imgAnnouncement = findViewById(R.id.imgAnnouncement)
        tvNoAnnouncements = findViewById(R.id.tvNoAnnouncements)

        // Check if there's an announcement image passed
        val imageUriString = intent.getStringExtra("announcement_image_uri")
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            imgAnnouncement.setImageURI(imageUri)
            imgAnnouncement.visibility = View.VISIBLE
            tvNoAnnouncements.visibility = View.GONE
        } else {
            // Default state: No announcements available
            imgAnnouncement.visibility = View.GONE
            tvNoAnnouncements.visibility = View.VISIBLE
        }
    }
}
