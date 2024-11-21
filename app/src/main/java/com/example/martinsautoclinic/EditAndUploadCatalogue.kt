package com.example.martinsautoclinic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.END
import androidx.recyclerview.widget.ItemTouchHelper.START
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.martinsautoclinic.databinding.ActivityEditAndUploadCatalogueBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class EditAndUploadCatalogue : AppCompatActivity() {

    private lateinit var binding: ActivityEditAndUploadCatalogueBinding;
    private val listOfList : MutableList<MutableList<dataClassServicesObjects>> = mutableListOf()

    var serviceList: MutableList<dataClassServicesObjects> = mutableListOf()

    private val retrievedRecords = mutableListOf<Pair<String,String>>()
    private val CopyretrievedRecords = mutableListOf<Pair<String,String>>()
    private lateinit var database: DatabaseReference
    private lateinit var adapter: AdapterCatalogueRecyclerView
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditAndUploadCatalogueBinding.inflate(layoutInflater);
        setContentView(binding.root);

        lifecycleScope.launch {

            val testingData = retrieveServicesFromDatabase("TestingDB")
            withContext(Dispatchers.Main) {
                adapter.updateServices(testingData)
                binding.RecyclerViewEditCatalogue.adapter?.notifyDataSetChanged()
            }

        }


        val buttonSave = findViewById<Button>(R.id.buttonSave)
        val backToMainCatalogue = findViewById<Button>(R.id.backToMainCatalogue)
        val retrieveCatalog = findViewById<Button>(R.id.buttonRetrieveCatalogue)

        backToMainCatalogue.setOnClickListener{
            val intent = Intent(this, ServicesActivity::class.java)
            startActivity(intent)
        }
        val buttonAddServicePage = findViewById<Button>(R.id.buttonAddServicePage)
        buttonAddServicePage.setOnClickListener {
            val intent = Intent(this, AddService::class.java)
            startActivity(intent)
        }

        retrieveCatalog.setOnClickListener {
            lifecycleScope.launch {
                showLoadingOverlay();
                performSequentialTasks()
                hideLoadingOverlay()
            }
        }



        val btn = findViewById<Button>(R.id.button2)

        btn.setOnClickListener {

            lifecycleScope.launch {

                database = FirebaseDatabase.getInstance().getReference("TestingDB");
                database.setValue(null);

                val testingData = retrieveServicesFromDatabase("TestingDB")
                withContext(Dispatchers.Main) {
                    adapter.updateServices(testingData)
                    binding.RecyclerViewEditCatalogue.adapter?.notifyDataSetChanged()
                }
            }
        }


        buttonSave.setOnClickListener {

            AssigningOrder(serviceList)

            val listLenght = serviceList.size - 1;
            for (x in 0..listLenght){
                updateingData(serviceList[x].primaryKey,serviceList[x].serviceName,serviceList[x].servicePrice,serviceList[x].serviceType,serviceList[x].orderNumber)
            }
            Toast.makeText(this, "Successfull", Toast.LENGTH_SHORT).show()

            val database = FirebaseDatabase.getInstance().getReference("TestingDB")
            database.removeValue()
            lifecycleScope.launch {
                val testingData = retrieveServicesFromDatabase("TestingDB")
                adapter.updateServices(testingData)
                binding.RecyclerViewEditCatalogue.adapter?.notifyDataSetChanged()
            }
        }


        adapter = AdapterCatalogueRecyclerView(serviceList) { selectedItem ->
            val foundObject = searchValue(serviceList,selectedItem)

            val intent = Intent(this, ItemInformationChanges::class.java)
            if (foundObject != null) {
                intent.putExtra("primaryKey", foundObject)
            }
            startActivity(intent)
        }


        binding.RecyclerViewEditCatalogue.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@EditAndUploadCatalogue)
            adapter = this@EditAndUploadCatalogue.adapter
        }

        itemTouchHelper.attachToRecyclerView(binding.RecyclerViewEditCatalogue)

    }

    suspend fun performSequentialTasks() {
        withContext(Dispatchers.IO) {

            val productsData = retrieveServicesFromDatabase("Products")

            writeServicesToDatabase("TestingDB", productsData)

            val testingData = retrieveServicesFromDatabase("TestingDB")
            withContext(Dispatchers.Main) {
                adapter.updateServices(testingData)
                binding.RecyclerViewEditCatalogue.adapter?.notifyDataSetChanged()
            }
        }
    }

    private suspend fun retrieveServicesFromDatabase(databasePath: String): List<dataClassServicesObjects> {
        return withContext(Dispatchers.IO) {
            val services = mutableListOf<dataClassServicesObjects>()
            val dbRef = FirebaseDatabase.getInstance().getReference(databasePath)

            suspendCoroutine<List<dataClassServicesObjects>> { continuation ->
                dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (recordSnapshot in snapshot.children) {
                            val primaryKey = recordSnapshot.key
                            val serviceName = recordSnapshot.child("serviceName").getValue(String::class.java)
                            val servicePrice = recordSnapshot.child("servicePrice").getValue(String::class.java)
                            val serviceType = recordSnapshot.child("serviceType").getValue(String::class.java)
                            val serviceOrder = recordSnapshot.child("orderNumber").getValue(Int::class.java)

                            if (primaryKey != null && serviceName != null && servicePrice != null && serviceType != null) {
                                services.add(
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
                        continuation.resume(services.sortedBy { it.orderNumber })
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("ServiceActivity", "Error")
                    }
                })
            }
        }
    }

    private suspend fun writeServicesToDatabase(databasePath: String, services: List<dataClassServicesObjects>) {
        val dbRef = FirebaseDatabase.getInstance().getReference(databasePath)

        withContext(Dispatchers.IO) {
            services.forEach { service ->
                suspendCoroutine<Unit> { continuation ->
                    dbRef.child(service.primaryKey).setValue(service)
                        .addOnSuccessListener {
                            continuation.resumeWith(Result.success(Unit))
                        }
                        .addOnFailureListener { error ->
                            continuation.resumeWith(Result.failure(error))
                        }
                }
            }
        }
    }


    private fun searchValue(myList:MutableList<dataClassServicesObjects>,id:String):String?{

        for(x in myList){
            if(x.serviceName == id){
                return x.primaryKey
            }
        }
        return null
    }


    private val itemTouchHelper by lazy {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            UP or DOWN or START or END, 0 // Drag directions
        ) {
            override fun onMove(
                RecyclerViewEditCatalogue: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                val adapter = RecyclerViewEditCatalogue.adapter as AdapterCatalogueRecyclerView

                // Call the onItemMove method of the adapter
                adapter.onItemMove(fromPosition, toPosition)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // No swipe functionality in this case
            }
        }
        ItemTouchHelper(itemTouchHelperCallback)
    }

    private fun AssigningOrder(nestedList:MutableList<dataClassServicesObjects>){

        val counter = (nestedList.size)-1

        for (x in 0..counter) {
            nestedList[x].orderNumber = x
        }
    }

    private fun updateingData(privateKey:String,headingOrServiceName:String,servicePrice:String,type:String,order:Int?){

        dbRef = FirebaseDatabase.getInstance().getReference("Products");

        val service = dataClassServicesObjects(privateKey,headingOrServiceName,servicePrice,type,order)

        dbRef.child(privateKey).setValue(service).addOnCompleteListener{

            Toast.makeText(this,"Data has been inserted",Toast.LENGTH_LONG).show()

        }.addOnFailureListener{ err->

            Toast.makeText(this,"Data has failed to inserted",Toast.LENGTH_LONG).show()
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