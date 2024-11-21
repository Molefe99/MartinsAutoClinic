package com.example.martinsautoclinic

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
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
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

class ServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServicesBinding;
    val serviceList: MutableList<dataClassServicesObjects> = mutableListOf()
    private lateinit var adapter: AdapterCatalogueRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServicesBinding.inflate(layoutInflater);
        setContentView(binding.root);

        val buttonBackToMain = findViewById<Button>(R.id.btnBack);
        val buttonEditCatalog = findViewById<Button>(R.id.btnBackToHome)

        buttonEditCatalog.setOnClickListener {
            val intent = Intent(this, EditAndUploadCatalogue::class.java)
            startActivity(intent)
        }

        buttonBackToMain.setOnClickListener {
            val intent = Intent(this, AdminDashboard::class.java)
            startActivity(intent)
        }

         adapter = AdapterCatalogueRecyclerView(serviceList);
            lifecycleScope.launch {
                showLoadingOverlay()
                try {
                    val data = performSequentialTasks()
                    adapter = AdapterCatalogueRecyclerView(data)
                    binding.RecyclerViewCurrent.adapter = adapter
                } catch (e: Exception) {
                    Log.d("ServiceActivity", "Error")
                } finally {
                    hideLoadingOverlay()
                }
            }

        binding.RecyclerViewCurrent.setHasFixedSize(true)
        binding.RecyclerViewCurrent.layoutManager = LinearLayoutManager(this)
        binding.RecyclerViewCurrent.adapter = adapter;
    }

    private suspend fun performSequentialTasks(): MutableList<dataClassServicesObjects> {
        return withContext(Dispatchers.IO) {
            val database = FirebaseDatabase.getInstance().getReference("Products")

            suspendCancellableCoroutine<MutableList<dataClassServicesObjects>> { continuation ->
                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        serviceList.clear()
                        for (recordSnapshot in snapshot.children) {
                            val primaryKey = recordSnapshot.key
                            val serviceName = recordSnapshot.child("serviceName").getValue(String::class.java)
                            val servicePrice = recordSnapshot.child("servicePrice").getValue(String::class.java)
                            val serviceType = recordSnapshot.child("serviceType").getValue(String::class.java)
                            val serviceOrder = recordSnapshot.child("orderNumber").getValue(Int::class.java)

                            if (primaryKey != null && serviceName != null && servicePrice != null && serviceType != null) {
                                serviceList.add(
                                    dataClassServicesObjects(
                                        primaryKey, serviceName, servicePrice, serviceType, serviceOrder
                                    )
                                )
                            }
                        }
                        serviceList.sortBy { it.orderNumber }
                        continuation.resume(serviceList) { throwable -> continuation.cancel(throwable) }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWith(Result.failure(error.toException()))
                    }
                })
            }
        }
    }

    private fun showLoadingOverlay() {
        findViewById<View>(R.id.loadingOverlay).visibility = View.VISIBLE
    }

    private fun hideLoadingOverlay() {
        findViewById<View>(R.id.loadingOverlay).visibility = View.GONE
    }
}