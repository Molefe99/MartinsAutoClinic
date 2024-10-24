package com.example.martinsautoclinic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashbord);
    }

    public void Change(View view)
    {
        //imgAppointment.findViewById(R.id.imgAppointment);
        //txtAppointment.findViewById(R.id.txtAppointment);

        Intent intent = new Intent(this, BookAppointment.class);
        startActivity(intent);
    }
}