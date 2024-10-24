package com.example.martinsautoclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    ImageView imgAppointment;
    TextView txtAppointment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void Change(View view)
    {
        //imgAppointment.findViewById(R.id.imgAppointment);
        //txtAppointment.findViewById(R.id.txtAppointment);

        Intent intent = new Intent(this, AdminDashboard.class);
        startActivity(intent);
    }
}