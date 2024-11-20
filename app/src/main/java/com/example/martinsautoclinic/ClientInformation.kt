package com.example.martinsautoclinic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import android.widget.TextView

class ClientInformationActivity : AppCompatActivity() {

    private lateinit var viewModel: ClientViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_information)
        val backButton: Button = findViewById(R.id.back_button)

        // Initialize Database, Repository, and ViewModel
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = ClientRepository(database.clientDao())
        val factory = ClientViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ClientViewModel::class.java]

        // Observe LiveData to update UI
        viewModel.client.observe(this) { client ->
            displayClientInfo(client)
        }

        // Fetch client data by ID
        viewModel.fetchClient(clientId = 1) // Replace with dynamic ID if needed

        // Handle Back Button Click
        backButton.setOnClickListener {
            val intent = Intent(this, AdminDashboard::class.java)
            startActivity(intent)
            finish() // Optional: closes the current activity
        }
    }

    private fun displayClientInfo(client: Client) {
        findViewById<TextView>(R.id.client_name).text = client.name
        findViewById<TextView>(R.id.client_phone).text = client.phone
        findViewById<TextView>(R.id.client_email).text = client.email
    }
}
