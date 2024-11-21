package com.example.martinsautoclinic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Objects;

public class InvoiceActivity extends AppCompatActivity {

    //Button btnBook;
    private TextView txtBookedDate;
    private TextView txtBookiePhone;
    private TextView txtBookieEmail;
    private TextView txtBookedServices;
    private TextView txtBookedServicesPrice;
    private TextView txtSubTotal;
    private TextView txtVAT;
    private TextView txtGrandTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_invoice);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        txtBookedDate = findViewById(R.id.txtBookedDate);
        txtBookiePhone = findViewById(R.id.txtClientPhone);
        txtBookieEmail = findViewById(R.id.txtClientEmail);
        txtBookedServices = findViewById(R.id.txtBookedServices);
        txtBookedServicesPrice = findViewById(R.id.txtBookedServicesPrice);
        txtSubTotal = findViewById(R.id.txtSubTotalPrice);
        txtVAT = findViewById(R.id.txtVATPrice);
        txtGrandTotal = findViewById(R.id.txtGrandTotalPrice);

        double calcVAT = (getIntent().getDoubleExtra("keyTotalPrice", 0.00) * ((double) 15 /100));
        double calcSubTotal = getIntent().getDoubleExtra("keyTotalPrice", 0.00) - calcVAT;
        double getGrandTotal = getIntent().getDoubleExtra("keyTotalPrice", 0.00);

        String VAT = String.valueOf(calcVAT);
        String subtotal = String.valueOf(calcSubTotal);
        String grandTotal = String.valueOf(getGrandTotal);

        txtBookedDate.setText(getIntent().getStringExtra("keyDate"));
        txtBookiePhone.setText(getIntent().getStringExtra("keyPhone"));
        txtBookieEmail.setText(getIntent().getStringExtra("keyEmail"));
        txtBookedServices.setText(getIntent().getStringExtra("keyService"));
        txtBookedServicesPrice.setText(getIntent().getStringExtra("keyServicePrice"));
        txtSubTotal.setText(subtotal);
        txtVAT.setText(VAT);
        txtGrandTotal.setText(grandTotal);

    }

    public void CompleteBooking(View view)
    {
        Intent intent = new Intent(this, BookingSuccessful.class);
        startActivity(intent);
    }


}