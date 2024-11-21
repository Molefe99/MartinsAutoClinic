package com.example.martinsautoclinic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashbord);
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("AdminDashboard", "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("AdminDashboard", "onStop called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("AdminDashboard", "onResume called");
    }

    //Redirecting to Appointments Screen
    public void Appointments(View view)
    {
        //imgAppointment.findViewById(R.id.imgAppointment);
        //txtAppointment.findViewById(R.id.txtAppointment);

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish(); // Optional: Prevents activity stacking
        //Intent intent = new Intent(this, BookAppointment.class);
        //startActivity(intent);
    }

    //Redirecting to Quotes Screen
    public void Quotes(View view)
    {
        //imgAppointment.findViewById(R.id.imgAppointment);
        //txtAppointment.findViewById(R.id.txtAppointment);

        Intent intent = new Intent(this, QuotesActivity.class);
        startActivity(intent);
        finish();
    }

    //Redirecting to Services Screen
    public void Services(View view)
    {
        //imgAppointment.findViewById(R.id.imgAppointment);
        //txtAppointment.findViewById(R.id.txtAppointment);

        Intent intent = new Intent(this, ServicesActivity.class);
        startActivity(intent);
        finish();
    }

    //Redirecting to ClientsInfo Screen
    public void ClientsInfo(View view)
    {
        //imgAppointment.findViewById(R.id.imgAppointment);
        //txtAppointment.findViewById(R.id.txtAppointment);

        Intent intent = new Intent(this, ClientsActivity.class);
        startActivity(intent);
        finish();
    }

    //Redirecting to Announcements Screen
    public void Announcements(View view)
    {
        //imgAppointment.findViewById(R.id.imgAppointment);
        //txtAppointment.findViewById(R.id.txtAppointment);

        Intent intent = new Intent(this, SetAnnouncementsActivity.class);
        startActivity(intent);
        finish();
    }

    //Redirecting to Announcements Screen
    public void ComingSoon(View view)
    {
        String message = "Coming Soon!";

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}