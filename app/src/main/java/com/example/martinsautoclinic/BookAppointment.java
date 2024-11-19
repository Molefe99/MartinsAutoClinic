package com.example.martinsautoclinic;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookAppointment extends AppCompatActivity {

    final Calendar calendar = Calendar.getInstance();
    EditText edtCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_appointment);

        edtCalendar = findViewById(R.id.edttxtDate);

        edtCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(BookAppointment.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String myFormat = "dd-MMM-yyyy";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);

                        edtCalendar.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    public void Book(View view)
    {
        Intent intent = new Intent(this, InvoiceActivity.class);
        startActivity(intent);
    }

    public void CancelBooking(View view)
    {
        Intent intent = new Intent(this, AdminDashboard.class);
        startActivity(intent);
    }
}