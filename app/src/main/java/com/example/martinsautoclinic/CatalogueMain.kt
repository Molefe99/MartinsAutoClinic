package com.example.martinsautoclinic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.martinsautoclinic.databinding.ActivityCatalogueMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CatalogueMain : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogueMainBinding;
    private lateinit var database: DatabaseReference;
    private val retrievedRecords = mutableListOf<Pair<String, String>>()
    val serviceList: MutableList<dataClassServicesObjects> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogue_main)

        binding = ActivityCatalogueMainBinding.inflate(layoutInflater);
        setContentView(binding.root);

        retrievedRecords.clear()


        val buttonAddServicePage = findViewById<Button>(R.id.dewefwewe);
        val buttonEditCatalog = findViewById<Button>(R.id.buttonEditCatalog)

        //button to next screen
        buttonEditCatalog.setOnClickListener {
            val intent = Intent(this, EditAndUploadCatalogue::class.java)
            startActivity(intent)
        }

        //button to next screen
        buttonAddServicePage.setOnClickListener {
            val intent = Intent(this, RequestQuote::class.java)
            startActivity(intent)
            // val intent = Intent(this, AddService::class.java)
            //  startActivity(intent)
        }


        val adapter12 = AdapterCatalogueRecyclerView(serviceList);


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
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.RecyclerViewCurrent.setHasFixedSize(true)
        binding.RecyclerViewCurrent.layoutManager = LinearLayoutManager(this)


        binding.RecyclerViewCurrent.adapter = adapter12;


    }
    private fun hello(o:String){
        Toast.makeText(this,o, Toast.LENGTH_LONG).show()

    }
    /*    private fun retrieveData() {


            database = FirebaseDatabase.getInstance().getReference("Products");
            //database.child().get().addOnSuccessListener {}.addOnFailureListener{}
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    retrievedRecords.clear();

                    for (recordSnapshot in snapshot.children) {

                        val serviceName =
                            recordSnapshot.child("serviceName").getValue(String::class.java)
                        val serviceName1 =
                            recordSnapshot.child("servicePrice").getValue(String::class.java)

                        if (serviceName != null && serviceName1 != null) {
                            retrievedRecords.add(Pair(serviceName, serviceName1));

                        }
                    }
                    //adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    */
    //private fun Displaydata(nam: mutableListOf<Pair<String, String>>){
    private fun AssigningOrder(nestedList:MutableList<dataClassServicesObjects>){

        val counter = (nestedList.size)-1

        for (x in 0..counter) {
            nestedList[x].orderNumber = x
        }
    }
}