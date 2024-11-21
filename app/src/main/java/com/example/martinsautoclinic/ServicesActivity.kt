package com.example.martinsautoclinic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.martinsautoclinic.databinding.ActivityServicesBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServicesBinding;
    private lateinit var database: DatabaseReference;
    private val retrievedRecords = mutableListOf<Pair<String, String>>()
    val serviceList: MutableList<dataClassServicesObjects> = mutableListOf()
    private lateinit var adapter12: AdapterCatalogueRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityServicesBinding.inflate(layoutInflater);
        setContentView(binding.root);

        retrievedRecords.clear()

        val buttonBackToMain = findViewById<Button>(R.id.btnBack);
        val buttonEditCatalog = findViewById<Button>(R.id.btnBackToHome)

        //button to next screen
        buttonEditCatalog.setOnClickListener {
            val intent = Intent(this, EditAndUploadCatalogue::class.java)
            startActivity(intent)
            /*val intent = Intent(this, RequestQuote::class.java)
            startActivity(intent)*/
        }

        //button to next screen
        buttonBackToMain.setOnClickListener {
            val intent = Intent(this, AdminDashboard::class.java)
            startActivity(intent)

        }

         adapter12 = AdapterCatalogueRecyclerView(serviceList);
            lifecycleScope.launch {
                showLoadingOverlay();
                performSequentialTasks()
                hideLoadingOverlay()
            }






        binding.RecyclerViewCurrent.setHasFixedSize(true)
        binding.RecyclerViewCurrent.layoutManager = LinearLayoutManager(this)

        binding.RecyclerViewCurrent.adapter = adapter12;

    }

    suspend fun performSequentialTasks() {
        withContext(Dispatchers.IO) {
            database = FirebaseDatabase.getInstance().getReference("Products");

            //retrieve values in database
            database.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    retrievedRecords.clear();
                    for (recordSnapshot in snapshot.children) {

                        val primaryKey =  recordSnapshot.key
                        val serviceName =
                            recordSnapshot.child("serviceName").getValue(String::class.java)
                        val servicePrice =
                            recordSnapshot.child("servicePrice").getValue(String::class.java)
                        val serviceType =
                            recordSnapshot.child("serviceType").getValue(String::class.java)
                        val serviceOrder =
                            recordSnapshot.child("orderNumber").getValue(Int::class.java)

                        if (serviceName != null && primaryKey != null && servicePrice != null && serviceType != null) {
                            serviceList.add(dataClassServicesObjects(primaryKey, serviceName,servicePrice,serviceType,serviceOrder))
                        }
                    }
                    val serviceList123 = serviceList.sortedBy { it.orderNumber }.toMutableList()
                    adapter12.updateServices(serviceList123) // Update the adapter with the sorted list
                    (binding.RecyclerViewCurrent.adapter as? AdapterCatalogueRecyclerView)?.notifyDataSetChanged()
                    //hideLoadingOverlay();
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error retrieving data: ${error.message}")
                }
            })
        }
    }


    // Show loading overlay
    private fun showLoadingOverlay() {
        findViewById<View>(R.id.loadingOverlay).visibility = View.VISIBLE
    }

    // Hide loading overlay
    private fun hideLoadingOverlay() {
        findViewById<View>(R.id.loadingOverlay).visibility = View.GONE
    }
}