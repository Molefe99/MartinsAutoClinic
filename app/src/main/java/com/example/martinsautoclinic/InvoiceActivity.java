package com.example.martinsautoclinic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InvoiceActivity extends AppCompatActivity {

    //Button btnBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_invoice);

        //btnBook = findViewById(R.id.btnCompleteBooking);

//        btnBook.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//
//            }
//        });
    }

    public void CompleteBooking(View view)
    {
        Intent intent = new Intent(this, BookingSuccessful.class);
        startActivity(intent);
    }


}