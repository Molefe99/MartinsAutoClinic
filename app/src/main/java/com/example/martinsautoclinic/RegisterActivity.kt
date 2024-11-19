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

class RegisterActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etEmailAddress: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView

    private lateinit var firebaseAuth: FirebaseAuth

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

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Set click listener for the register button
        btnRegister.setOnClickListener {
            validateInputs()
        }

        // Redirect to login page if user already has an account
        tvLogin.setOnClickListener {
            redirectToLogin()
        }
    }

    private fun validateInputs() {
        // Get input values
        val username = etUsername.text.toString().trim()
        val phoneNumber = etPhoneNumber.text.toString().trim()
        val emailAddress = etEmailAddress.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        // Validate username
        if (TextUtils.isEmpty(username)) {
            showToast("Please enter a username")
            return
        }

        // Validate phone number
        if (TextUtils.isEmpty(phoneNumber)) {
            showToast("Please enter a phone number")
            return
        } else if (!isValidPhoneNumber(phoneNumber)) {
            showToast("Please enter a valid phone number")
            return
        }

        // Validate email address
        if (TextUtils.isEmpty(emailAddress)) {
            showToast("Please enter an email address")
            return
        } else if (!isValidEmail(emailAddress)) {
            showToast("Please enter a valid email address")
            return
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            showToast("Please enter a password")
            return
        } else if (TextUtils.isEmpty(confirmPassword)) {
            showToast("Please confirm your password")
            return
        } else if (password != confirmPassword) {
            showToast("Passwords do not match")
            return
        }

        // Firebase Authentication sign up
        firebaseAuth.createUserWithEmailAndPassword(emailAddress, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    showToast("Registration successful")
                    redirectToLogin()
                } else {
                    showToast("Error: ${task.exception?.message}")
                }
            }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        // Example: at least 10 digits for a phone number
        return phoneNumber.length >= 10
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close RegisterActivity
    }
}
