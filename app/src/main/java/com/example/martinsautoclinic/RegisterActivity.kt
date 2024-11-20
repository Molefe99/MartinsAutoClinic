package com.example.martinsautoclinic

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etEmailAddress: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize views
        etUsername = findViewById(R.id.etUsername)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etEmailAddress = findViewById(R.id.etEmailAddress)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmailAddress.text.toString().trim()
            val phoneNumber = etPhoneNumber.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (validateInputs(username, email, phoneNumber, password, confirmPassword)) {
                registerUser(username, email, phoneNumber, password)
            }
        }

        tvLogin.setOnClickListener {
            // Redirect to login screen
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun validateInputs(
        username: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phoneNumber) ||
            TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)
        ) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun registerUser(username: String, email: String, phoneNumber: String, password: String) {
        // Format the email for Firebase key compatibility
        val formattedEmail = email.replace(".", ",")  // Firebase does not support periods in keys

        // Register the user using Firebase Authentication
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val currentUser = firebaseAuth.currentUser
                currentUser?.let { user ->
                    val userId = user.uid
                    val userMap = mapOf(
                        "username" to username,
                        "email" to email,
                        "phoneNumber" to phoneNumber
                    )

                    // Save additional user data in Firebase Realtime Database under the formatted email
                    databaseReference.child(formattedEmail).setValue(userMap).addOnCompleteListener { dbTask ->
                        if (dbTask.isSuccessful) {
                            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Failed to store user data.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                // Handle the error when email is already in use or other registration failures
                if (task.exception?.message?.contains("The email address is already in use") == true) {
                    Toast.makeText(this, "Email already in use. Please use another email.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

