package com.example.martinsautoclinic;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboard extends AppCompatActivity {

    private ImageView imgAppointment, imgQuote, imgService, imgComingSoon2, imgComingSoon3, imgComingSoon4, imgLogo;
    private TextView txtAppointment, txtQuote, txtService, txtComingSoon2, txtComingSoon3, txtComingSoon4, txtHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enables edge-to-edge rendering
        setContentView(R.layout.activity_admin_dashbord); // Replace with your layout file name

        // Initialize views
        initializeViews();

        // Set up click listeners
        setupClickListeners();
    }

    /**
     * Initialize all views from the XML layout.
     */
    private void initializeViews() {
        // Header and Logo
        txtHeader = findViewById(R.id.txtHeader);
        imgLogo = findViewById(R.id.imgLogo);

        // Appointment Module
        imgAppointment = findViewById(R.id.imgAppointment);
        txtAppointment = findViewById(R.id.txtAppointment);

        // Quote Module
        imgQuote = findViewById(R.id.imgQuote);
        txtQuote = findViewById(R.id.txtQuote);

        // Service Module
        imgService = findViewById(R.id.imgService);
        txtService = findViewById(R.id.txtService);

        // Client Information Module
        imgComingSoon2 = findViewById(R.id.imgComingSoon2);
        txtComingSoon2 = findViewById(R.id.txtComingSoon2);

        // Announcement Module
        imgComingSoon3 = findViewById(R.id.imgComingSoon3);
        txtComingSoon3 = findViewById(R.id.txtComingSoon3);

        // Coming Soon Module
        imgComingSoon4 = findViewById(R.id.imgComingSoon4);
        txtComingSoon4 = findViewById(R.id.txtComingSoon4);
    }

    /**
     * Set up click listeners for all cards.
     */
    private void setupClickListeners() {
        // Appointment Module
        imgAppointment.setOnClickListener(this::navigateToAppointment);
        txtAppointment.setOnClickListener(this::navigateToAppointment);

        // Quote Module
        imgQuote.setOnClickListener(view -> navigateToActivity(QuotesActivity.class));
        txtQuote.setOnClickListener(view -> navigateToActivity(QuotesActivity.class));

        // Service Module
        imgService.setOnClickListener(view -> navigateToActivity(ServiceActivity.class));
        txtService.setOnClickListener(view -> navigateToActivity(ServiceActivity.class));

        // Client Information Module
        imgComingSoon2.setOnClickListener(view -> navigateToActivity(ClientInformationActivity.class));
        txtComingSoon2.setOnClickListener(view -> navigateToActivity(ClientInformationActivity.class));

        // Announcement Module
        View imgAnnouncement;
        imgAnnouncement.setOnClickListener(view -> navigateToActivity(AnnouncementActivity.class));
        txtComingSoon3.setOnClickListener(view -> navigateToActivity(AnnouncementActivity.class));

        // Coming Soon Module
        imgComingSoon4.setOnClickListener(view -> showToast("Coming Soon!"));
        txtComingSoon4.setOnClickListener(view -> showToast("Coming Soon!"));
    }

    /**
     * Navigate to the Appointment activity. (Existing Change method)
     */
    public void navigateToAppointment(View view) {
        Intent intent = new Intent(this, BookAppointment.class);
        startActivity(intent);
    }

    /**
     * Navigate to a specific activity.
     *
     * @param targetActivity The target activity class.
     */
    private void navigateToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(AdminDashboard.this, targetActivity);
        startActivity(intent);
    }

    /**
     * Show a toast message.
     *
     * @param message The message to display.
     */
    private void showToast(String message) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
    }
}
