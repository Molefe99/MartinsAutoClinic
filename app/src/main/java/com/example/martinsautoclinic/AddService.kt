package com.example.martinsautoclinic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddService : AppCompatActivity() {

    private lateinit var buttonAddService: Button;
    private lateinit var buttonBacktoMainCatalogue:Button;
    private lateinit var checkboxHeading:CheckBox;
    private lateinit var checkboxService:CheckBox;
    private lateinit var editTextHeading :EditText;
    private lateinit var editTextServiceName :EditText;
    private lateinit var editTextServicePrice :EditText;
    private var passValidation : Boolean = false;
    private lateinit var dbRef : DatabaseReference;
    private final var Service :String = "ServiceData";
    private final var Heading : String = "HeadingData";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service)

        buttonAddService = findViewById(R.id.ButtonAddService);
        buttonBacktoMainCatalogue = findViewById(R.id.ButtonBackTomMainCatalogue1);

        checkboxHeading = findViewById(R.id.checkBoxHeading);
        checkboxService = findViewById(R.id.checkBoxService);

        editTextHeading = findViewById(R.id.editTextHeading);
        editTextServiceName = findViewById(R.id.editTextServiceName);
        editTextServicePrice = findViewById(R.id.editTextNumberServicePrice);

        //resetting components to default state
        startUp();

        //if header checkbox is true
        checkboxHeading.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                checkboxHeading.isEnabled = false;
                checkboxService.isEnabled = true;
                editTextHeading.isEnabled = true;
                checkboxService.isChecked = false;
                editTextServiceName.isEnabled = false;
                editTextServicePrice.isEnabled = false;
                editTextServiceName.text = null;
                editTextServicePrice.text = null;
                editTextServiceName.error = null;
                editTextServicePrice.error = null;
            }
        }

        //if Service checkbox is true
        checkboxService.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Uncheck checkBox1 when checkBox2 is checked
                checkboxHeading.isEnabled = true;
                checkboxService.isEnabled = false;
                checkboxHeading.isChecked = false
                editTextHeading.isEnabled = false;
                editTextServiceName.isEnabled = true;
                editTextServicePrice.isEnabled = true;

                editTextHeading.text = null;
                editTextHeading.error = null;
            }
        }

        //button to come back to main menu
        buttonBacktoMainCatalogue.setOnClickListener{
            val intent = Intent(this, EditAndUploadCatalogue::class.java)
            startActivity(intent)
        }

        //button to add the service or heading text to database
        buttonAddService.setOnClickListener {

            //if heading checkbox is true
            if (checkboxHeading.isChecked) {
                passValidation = ValidateData(editTextHeading.text.toString());

                //if validation pass
                if (passValidation){
                    //call insert method
                    insertingService(Heading,editTextHeading.text.toString());
                } else (startUp())

                //else if service checkbox is true
            } else if (checkboxService.isChecked) {

                //validate the user input
                passValidation = ValidateData(
                    editTextServiceName.text.toString(),
                    editTextServicePrice.text.toString()
                )

                //if validation pass
                if (passValidation) {
                    //call insert method
                    insertingService(
                        Service,
                        editTextServiceName.text.toString(),
                        editTextServicePrice.text.toString()
                    );

                }//if user input fail reset everything to default setting
                else (startUp())
            }
            //if user input fail reset everything to default setting
            startUp();
        }
    }

    private fun startUp(){

        passValidation = false;

        checkboxHeading.isChecked = true;
        editTextHeading.isEnabled = true;
        editTextServiceName.isEnabled = false;
        editTextServicePrice.isEnabled = false;
        checkboxHeading.isEnabled = false;

        editTextHeading.text = null;
        editTextServiceName.text = null;
        editTextServicePrice.text = null;

    }

    private fun ValidateData(serviceHeading :String): Boolean{
        var valid:Boolean = false;

        if (serviceHeading.isEmpty()) {
            editTextHeading.error = "Please Enter Heading";
        } else {
            valid = true
        }

        return valid;
    }

    private fun ValidateData(serviceName :String,servicePrice:String) : Boolean{
        var valid:Boolean = false;

        if (serviceName.isEmpty()){
            editTextServiceName.error = "Please Enter Service Name";
        }else if (servicePrice.isEmpty()){
            editTextServicePrice.error = "PLease Enter Service Price";
        } else {
            valid = true;
        }

        return valid;
    }

    //insert data into database
    private fun insertingService(type:String,headingOrServiceName:String,servicePrice:String = ""){

        val order = 0

        dbRef = FirebaseDatabase.getInstance().getReference("TestingDB");

        val privateKey = dbRef.push().key.toString();

        val service = dataClassServicesObjects(privateKey,headingOrServiceName,servicePrice,type,order)

        dbRef.child(privateKey).setValue(service).addOnSuccessListener{

            Toast.makeText(this,"Data has been inserted",Toast.LENGTH_LONG).show()

        }.addOnFailureListener{

            Toast.makeText(this,"Data has failed to inserted",Toast.LENGTH_LONG).show()
        }
    }
}
