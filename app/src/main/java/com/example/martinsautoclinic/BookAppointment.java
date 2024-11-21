package com.example.martinsautoclinic;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class BookAppointment extends AppCompatActivity {

    final Calendar calendar = Calendar.getInstance();
    EditText edtCalendar;
    EditText edtCell;
    EditText edtEmail;
    CheckBox chkStandard, chkMajor, chkSoftwareUpdate, chkMinor, chkPaintJob, chkTire;
    Double totalPrice = 0.00;
    Double VAT = 0.00;

    Bitmap bitmap, scaleBitmap;

    String MACCell = "(011)-515 2024";
    String MACEmail = "office@martinautoclinic.co.za";
    int pageWidth = 1200;
    String invoiceNumber = "12345";
    Date date;
    DateFormat dateFormat;

    String bankName = "First National Bank";
    String accountHolder = "Martins Auto Clinic";
    String bankAccount = "608512548963";
    String branchCode = "101056";
    String reference = "Your Invoice Number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_appointment);

        edtCalendar = findViewById(R.id.edttxtDate);
        edtCell = findViewById(R.id.edtCellphone);
        edtEmail = findViewById(R.id.edtEmail);

        chkStandard = findViewById(R.id.chkStandardService);
        chkMajor = findViewById(R.id.chkMajor);
        chkSoftwareUpdate = findViewById(R.id.chkSoftwareUpdate);
        chkMinor = findViewById(R.id.chkMinor);
        chkPaintJob = findViewById(R.id.chkPaintJob);
        chkTire = findViewById(R.id.chkTire);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo3);
        scaleBitmap = Bitmap.createScaledBitmap(bitmap, 620, 162, false);

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
        //Storing user input into variable
        String date =  edtCalendar.getText().toString();
        String phone = edtCell.getText().toString();
        String email = edtEmail.getText().toString();

        StringBuilder serviceList = new StringBuilder();
        StringBuilder servicePrice = new StringBuilder();


        //Checking if any of the services where checked
        if (chkStandard.isChecked())
        {
            serviceList.append("Standard \n");
            servicePrice.append("R3500.00 \n");
            totalPrice += 3500.00;
        }
        if (chkMajor.isChecked())
        {
            serviceList.append("Major Repair \n");
            servicePrice.append("R12750.60 \n");
            totalPrice += 12750.60;
        }
        if (chkSoftwareUpdate.isChecked())
        {
            serviceList.append("Software Update \n");
            servicePrice.append("R5900.00 \n");
            totalPrice += 5900.00;
        }
        if (chkMinor.isChecked())
        {
            serviceList.append("Minor Repair \n");
            servicePrice.append("R4000.00 \n");
            totalPrice += 4000.00;
        }
        if (chkPaintJob.isChecked())
        {
            serviceList.append("Paint Job \n");
            servicePrice.append("R6000.00  \n");
            totalPrice += 6000.00;
        }
        if (chkTire.isChecked())
        {
            serviceList.append("Tire \n");
            servicePrice.append("R1500.00 \n");
            totalPrice += 1500.00;
        }

        VAT = totalPrice * (15/100);
        String listServices = serviceList.toString();

        //Check if text fields are not empty
        if (edtCalendar.getText().length() > 0)
        {
            if (edtCell.getText().length() > 0)
            {
                if (edtEmail.getText().length() > 0)
                {
                    if (chkStandard.isChecked() || chkMajor.isChecked() || chkSoftwareUpdate.isChecked() || chkMinor.isChecked() || chkPaintJob.isChecked() || chkTire.isChecked())
                    {
                        ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

                        StoreToDB(date, phone, email, listServices, VAT, totalPrice);
                        //createInvoice();
                        //SendEmail(String.valueOf(edtEmail.getText()));
                        
                        Intent intent = new Intent(this, InvoiceActivity.class);
                        intent.putExtra("keyDate", date);
                        intent.putExtra("keyPhone", phone);
                        intent.putExtra("keyEmail", email);
                        intent.putExtra("keyService", (CharSequence) serviceList);
                        intent.putExtra("keyServicePrice", (CharSequence) servicePrice);
                        intent.putExtra("keyTotalPrice", totalPrice);

                        startActivity(intent);
                    }
                    else
                    {
                        String message = "Please choose service";
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    String message = "Please enter email address";

                    edtEmail.findFocus();
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                String message = "Please enter cellphone number";

                edtCell.findFocus();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            String message = "Please choose date";

            edtCalendar.findFocus();
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

    }

    private void StoreToDB(String date, String phone, String email, String listServices, Double vat, Double totalPrice)
    {
        //Create a hashmap
        HashMap<String, Object> bookingHashMap = new HashMap<>();

        bookingHashMap.put("booked date", date);
        bookingHashMap.put("client phone", phone);
        bookingHashMap.put("client email", email);
        bookingHashMap.put("booked service(s)", listServices);
        bookingHashMap.put("vat (15%)", vat);
        bookingHashMap.put("total price", totalPrice);

        //Instantiate database connection
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bookingsRef = database.getReference("Bookings");

        String bookingKey = bookingsRef.push().getKey();
        bookingHashMap.put("bookingKey", bookingKey);

        //Write to database
        bookingsRef.child(bookingKey).setValue(bookingHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Toast.makeText(BookAppointment.this, "Stored to database", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @SuppressLint("SimpleDateFormat")
    public void createInvoice()
    {
        date = new Date();
        int itemNum = 0;

        PdfDocument myInvoice = new PdfDocument();
        Paint myPaint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
        PdfDocument.Page myPage = myInvoice.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();

        canvas.drawBitmap(scaleBitmap, pageWidth/2, 0, myPaint);

        myPaint.setColor(Color.rgb(192, 194, 196));
        myPaint.setTextSize(30f);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Call: "+MACCell, 1160, 40, myPaint);
        canvas.drawText("Email: "+MACEmail, 1160, 80, myPaint);

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        titlePaint.setTextSize(70);
        canvas.drawText("Invoice", pageWidth/2, 500, titlePaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(35f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("Customer Email: "+edtEmail.getText(), 20, 590, myPaint);
        canvas.drawText("Customer No.: "+edtCell.getText(), 20, 640, myPaint);

        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Invoice No.: "+invoiceNumber, pageWidth - 20, 590, myPaint);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        canvas.drawText("Date: "+dateFormat.format(date), pageWidth - 20, 640, myPaint);

        dateFormat = new SimpleDateFormat("HH:mm:ss");
        canvas.drawText("Time: "+dateFormat.format(date), pageWidth - 20, 690, myPaint);

        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(2);
        canvas.drawRect(20, 780, pageWidth - 20, 860, myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setStyle(Paint.Style.FILL);
        canvas.drawText("Item No.", 40, 830, myPaint);
        canvas.drawText("Description", 200, 830, myPaint);
        canvas.drawText("Price", 700, 830, myPaint);
        canvas.drawText("Qty.", 900, 830, myPaint);
        canvas.drawText("Total", 1050, 830, myPaint);

        canvas.drawLine(180, 790, 180, 840, myPaint);
        canvas.drawLine(680, 790, 680, 840, myPaint);
        canvas.drawLine(880, 790, 880, 840, myPaint);
        canvas.drawLine(1030, 790, 1030, 840, myPaint);

        double miniTotal = 0;
        if (chkStandard.isChecked())
        {
            itemNum++;
            canvas.drawText(itemNum+".", 40, 950, myPaint);
            canvas.drawText("Standard Service", 200, 950, myPaint);
            canvas.drawText("3500.00", 700, 950, myPaint);
            canvas.drawText("1", 900, 950, myPaint);
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("3500.00", pageWidth - 40, 950, myPaint);
            myPaint.setTextAlign(Paint.Align.LEFT);
        }

        if (chkMajor.isChecked())
        {
            itemNum++;
            canvas.drawText(itemNum+".", 40, 1050, myPaint);
            canvas.drawText("Major Repair", 200, 1050, myPaint);
            canvas.drawText("12750.60", 700, 1050, myPaint);
            canvas.drawText("1", 900, 1050, myPaint);
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("12750.60", pageWidth - 40, 1050, myPaint);
            myPaint.setTextAlign(Paint.Align.LEFT);
        }

        if (chkSoftwareUpdate.isChecked())
        {
            itemNum++;
            canvas.drawText(itemNum+".", 40, 1150, myPaint);
            canvas.drawText("Software Update", 200, 1150, myPaint);
            canvas.drawText("5900.00", 700, 1150, myPaint);
            canvas.drawText("1", 900, 1150, myPaint);
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("5900.00", pageWidth - 40, 1150, myPaint);
            myPaint.setTextAlign(Paint.Align.LEFT);
        }
        //6000.00, 1500.00
        if (chkMinor.isChecked())
        {
            itemNum++;
            canvas.drawText(itemNum+".", 40, 1250, myPaint);
            canvas.drawText("Minor Repair", 200, 1250, myPaint);
            canvas.drawText("4000.00", 700, 1250, myPaint);
            canvas.drawText("1", 900, 1250, myPaint);
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("4000.00", pageWidth - 40, 1250, myPaint);
            myPaint.setTextAlign(Paint.Align.LEFT);
        }

        if (chkPaintJob.isChecked())
        {
            itemNum++;
            canvas.drawText(itemNum+".", 40, 1350, myPaint);
            canvas.drawText("Paint Job", 200, 1350, myPaint);
            canvas.drawText("6000.00", 700, 1350, myPaint);
            canvas.drawText("1", 900, 1350, myPaint);
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("6000.00", pageWidth - 40, 1350, myPaint);
            myPaint.setTextAlign(Paint.Align.LEFT);
        }

        if (chkTire.isChecked())
        {
            itemNum++;
            canvas.drawText(itemNum+".", 40, 1450, myPaint);
            canvas.drawText("Tire Repair", 200, 1450, myPaint);
            canvas.drawText("1500.00", 700, 1450, myPaint);
            canvas.drawText("1", 900, 1450, myPaint);
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("1500.00", pageWidth - 40, 1450, myPaint);
            myPaint.setTextAlign(Paint.Align.LEFT);
        }

        canvas.drawLine(680, 1500, pageWidth - 20, 1500, myPaint);
        canvas.drawText("Sub-Total", 700, 1550, myPaint);
        canvas.drawText(":", 900, 1550, myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(totalPrice - VAT), pageWidth - 40, 1550, myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("VAT (15%)", 700, 1600, myPaint);
        canvas.drawText(":", 900, 1600, myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(VAT), pageWidth - 40, 1600, myPaint);
        myPaint.setTextAlign(Paint.Align.LEFT);

        myPaint.setColor(Color.rgb(184, 77, 24));
        canvas.drawRect(680, 1650, pageWidth - 20, 1750, myPaint);

        myPaint.setColor(Color.BLACK);
        myPaint.setTextSize(50f);
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Total", 700, 1715, myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(totalPrice), pageWidth - 40, 1715, myPaint);

        //Company Banking Details
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawLine(20, 1800, pageWidth - 20, 1800, myPaint);

        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        titlePaint.setTextSize(50);
        canvas.drawText("Banking Details", 50, 1850, titlePaint);

        myPaint.setTextSize(35f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("Bank", 50, 1860, myPaint);
        canvas.drawText(bankName, 100, 1860, myPaint);

        canvas.drawText("Account Holder", 50, 1870, myPaint);
        canvas.drawText(accountHolder, 100, 1870, myPaint);

        canvas.drawText("Account Number", 50, 1900, myPaint);
        canvas.drawText(bankAccount, 100, 1900, myPaint);

        canvas.drawText("Branch Code", 50, 1910, myPaint);
        canvas.drawText(branchCode, 100, 1910, myPaint);

        canvas.drawText("Reference", 50, 1920, myPaint);
        canvas.drawText(reference, 100, 1920, myPaint);

        myInvoice.finishPage(myPage);

        File file = new File(Environment.getExternalStorageDirectory(), "/MAC-Invoice.pdf");

        try 
        {
            myInvoice.writeTo(new FileOutputStream(file));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        myInvoice.close();

    }

    public void SendEmail(String toEmail)
    {
        String emailContent = "Good day sir/madam, \nPlease find attached invoice.  \n\nKind Regards, \nMartin's Auto Clinic";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{toEmail});
        intent.putExtra(Intent.EXTRA_SUBJECT, "MAC Invoice "+date.toString());
        intent.putExtra(Intent.EXTRA_TEXT, emailContent);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose email client: "));
    }

    public void CancelBooking(View view)
    {
        Intent intent = new Intent(this, AdminDashboard.class);
        startActivity(intent);
    }
}