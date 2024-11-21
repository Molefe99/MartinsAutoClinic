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
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ItemInformationChanges : AppCompatActivity() {

    private lateinit var  database: DatabaseReference;
    private lateinit var  serviceName:String
    private lateinit var  servicePrice:String
    private lateinit var  type:String

    private var isHeading:Boolean = false;
    private var isService:Boolean = false;

    private lateinit var editTextHeadingChanges : EditText;
    private lateinit var editTextServiceNameChanges : EditText;
    private lateinit var editTextNumberServicePriceChanges :EditText;

    private lateinit var checkBoxServiceChanges : CheckBox;
    private lateinit var checkBoxHeadingChanges : CheckBox;

    private var titleChanges : Boolean = false;
    private var SerivcesChanges : Boolean = false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_information_changes)

        editTextHeadingChanges = findViewById(R.id.editTextHeadingChanges);
        editTextServiceNameChanges = findViewById(R.id.editTextServiceNameChanges);
        editTextNumberServicePriceChanges = findViewById(R.id.editTextNumberServicePriceChanges);

        checkBoxServiceChanges = findViewById(R.id.checkBoxServiceChanges);
        checkBoxHeadingChanges = findViewById(R.id.checkBoxHeadingChanges);

        val buttonBackToEditCatalog = findViewById<Button>(R.id.buttonBackToEditCatalog)
        val buttonDeleteEntry = findViewById<Button>(R.id.buttonDeleteEntry)
        val buttonSaveChanges = findViewById<Button>(R.id.buttonSaveChanges)

        startUpLayout()


        val database1 = FirebaseDatabase.getInstance().getReference("TestingDB")

        buttonBackToEditCatalog.setOnClickListener{
            val intent = Intent(this, EditAndUploadCatalogue::class.java)
            startActivity(intent)
        }

        val intent = intent
        // Retrieve the string data passed with the key "item_data"
        val selectedItem = intent.getStringExtra("primaryKey").toString()


        if (selectedItem.isNotEmpty()) {
            retrieveInformation(selectedItem)
        }

        buttonDeleteEntry.setOnClickListener {
            database1.child(selectedItem).removeValue().addOnSuccessListener {

                Toast.makeText(this,"Item successfully deleted",Toast.LENGTH_SHORT).show();
                val intent = Intent(this, EditAndUploadCatalogue::class.java);
                startActivity(intent);

            }.addOnFailureListener {
                Toast.makeText(this,"Failed to delete item",Toast.LENGTH_LONG).show();
            }
        }

        buttonSaveChanges.setOnClickListener {

            val database12 = FirebaseDatabase.getInstance().getReference("TestingDB").child(selectedItem)

            if (isHeading) {

                if (editTextHeadingChanges.text.isNotEmpty()) {

                    val updateMap = mapOf(
                        "serviceName" to editTextHeadingChanges.text.toString()
                    )
                    database12.updateChildren(updateMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Update successful!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to update!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            if (isService) {

                if (editTextServiceNameChanges.text.isEmpty() && editTextNumberServicePriceChanges.text.isEmpty()) {
                    editTextNumberServicePriceChanges.error = "Enter Value";
                    editTextServiceNameChanges.error = "Enter Value";
                }

                if (editTextServiceNameChanges.text.isEmpty() && editTextNumberServicePriceChanges.text.isNotEmpty()) {
                    if (editTextNumberServicePriceChanges.text.isNotEmpty()) {

                        val updateMap = mapOf(
                            "servicePrice" to editTextNumberServicePriceChanges.text.toString()
                        )
                        database12.updateChildren(updateMap).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Update successful!", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(this, "Failed to update!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else if (editTextServiceNameChanges.text.isNotEmpty() && editTextNumberServicePriceChanges.text.isEmpty()) {

                    val updateMap = mapOf(
                        "serviceName" to editTextServiceNameChanges.text.toString(),
                    )
                    database12.updateChildren(updateMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Update successful!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to update!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                if (editTextServiceNameChanges.text.isNotEmpty() && editTextNumberServicePriceChanges.text.isNotEmpty()){
                    val updateMap = mapOf(
                        "serviceName" to editTextServiceNameChanges.text.toString(),
                        "servicePrice" to editTextNumberServicePriceChanges.text.toString(),
                    )
                    database12.updateChildren(updateMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Update successful!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to update!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }


    }

    private fun retrieveInformation(primaryKey: String) {
        lifecycleScope.launch {
            // Perform the task and wait for it to complete
            val isServiceData = performTask(primaryKey)

            // Now update the UI based on the fetched data
            if (isServiceData) {
                Toast.makeText(this@ItemInformationChanges, "Service data is true", Toast.LENGTH_LONG).show()

                checkBoxServiceChanges.isChecked = true
                checkBoxServiceChanges.isEnabled = false
                editTextServiceNameChanges.isEnabled = true
                editTextNumberServicePriceChanges.isEnabled = true

                editTextServiceNameChanges.hint = serviceName
                editTextNumberServicePriceChanges.hint = servicePrice
            } else if (isHeading) {
                checkBoxHeadingChanges.isChecked = true
                checkBoxHeadingChanges.isEnabled = false
                editTextHeadingChanges.isEnabled = true
                editTextHeadingChanges.hint = serviceName
            }
        }
    }

    private suspend fun performTask(primaryKey: String): Boolean {
        return withContext(Dispatchers.IO) {
            var isServiceData = false
            database = FirebaseDatabase.getInstance().getReference("TestingDB")

            // Perform the Firebase database fetch operation
            val snapshot =
                database.child(primaryKey).get().await()  // Using Kotlin's await() to ensure completion

            if (snapshot.exists()) {
                serviceName = snapshot.child("serviceName").value.toString()
                servicePrice = snapshot.child("servicePrice").value.toString()
                type = snapshot.child("serviceType").value.toString()

                // Determine the type of data (Heading or Service)
                if (type == "HeadingData") {
                    isHeading = true
                } else if (type == "ServiceData") {
                    isService = true
                    isServiceData = true
                }
            }
            isServiceData
        }
    }

    private fun startUpLayout(){

        checkBoxServiceChanges.isChecked =  false;
        checkBoxServiceChanges.isEnabled = false;

        editTextServiceNameChanges.isEnabled = false;
        editTextNumberServicePriceChanges.isEnabled = false;
        editTextHeadingChanges.isEnabled = false;

        checkBoxHeadingChanges.isChecked = false;
        checkBoxHeadingChanges.isEnabled = false;
    }

}