package com.example.martinsautoclinic

import android.annotation.SuppressLint
import kotlinx.coroutines.suspendCancellableCoroutine
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.martinsautoclinic.databinding.ActivityClientViewPricingBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClientViewPricing : AppCompatActivity() {

    private lateinit var binding: ActivityClientViewPricingBinding;
    private lateinit var database: DatabaseReference;
    private val retrievedRecords = mutableListOf<Pair<String, String>>()
    val serviceList: MutableList<dataClassServicesObjects> = mutableListOf()
    private lateinit var adapter12: AdapterCatalogueRecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Properly initialize the binding
        binding = ActivityClientViewPricingBinding.inflate(layoutInflater)
        setContentView(binding.root) // Use the root view of the binding

        // Initialize RecyclerView
        binding.RecyclerViewCurrent2.layoutManager = LinearLayoutManager(this) // Attach a layout manager
        binding.RecyclerViewCurrent2.setHasFixedSize(true)

        binding.RecyclerViewCurrent2.layoutManager = LinearLayoutManager(this) // Attach layout manager
        binding.RecyclerViewCurrent2.setHasFixedSize(true)

        val btnBackToHome = findViewById<Button>(R.id.btnBackToHome);

        btnBackToHome.setOnClickListener {

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

        }

        adapter12 = AdapterCatalogueRecyclerView(serviceList)
        binding.RecyclerViewCurrent2.adapter = adapter12

        lifecycleScope.launch {
            showLoadingOverlay()
            try {
                DisplayRecords() // Waits for data retrieval to finish
            } catch (e: Exception) {
                Toast.makeText(this@ClientViewPricing, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                hideLoadingOverlay()
                Toast.makeText(this@ClientViewPricing, "Data Loaded", Toast.LENGTH_SHORT).show()
            }  }

    }


    @SuppressLint("NotifyDataSetChanged")
    suspend fun DisplayRecords() {
        withContext(Dispatchers.IO) {
            val serviceList123 = fetchRecordsFromFirebase().toMutableList()
            withContext(Dispatchers.Main) {
                binding.RecyclerViewCurrent2.adapter = adapter12
                adapter12.updateServices(serviceList123)
                adapter12.notifyDataSetChanged()
            }
        }
    }

    private suspend fun fetchRecordsFromFirebase(): MutableList<dataClassServicesObjects> =
        withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { continuation ->
                val database = FirebaseDatabase.getInstance().getReference("Products")
                val retrievedList = mutableListOf<dataClassServicesObjects>()

                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        retrievedList.clear()
                        for (recordSnapshot in snapshot.children) {
                            val primaryKey = recordSnapshot.key
                            val serviceName = recordSnapshot.child("serviceName").getValue(String::class.java)
                            val servicePrice = recordSnapshot.child("servicePrice").getValue(String::class.java)
                            val serviceType = recordSnapshot.child("serviceType").getValue(String::class.java)
                            val serviceOrder = recordSnapshot.child("orderNumber").getValue(Int::class.java)

                            if (serviceName != null && primaryKey != null && servicePrice != null && serviceType != null) {
                                retrievedList.add(
                                    dataClassServicesObjects(
                                        primaryKey,
                                        serviceName,
                                        servicePrice,
                                        serviceType,
                                        serviceOrder
                                    )
                                )
                            }
                        }
                        retrievedList.sortBy { it.orderNumber }
                        continuation.resume(retrievedList) { throwable ->
                            continuation.cancel(throwable)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWith(Result.failure(error.toException()))
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
