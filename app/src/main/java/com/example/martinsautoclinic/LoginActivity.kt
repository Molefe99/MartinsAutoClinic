package com.example.martinsautoclinic

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmailLogin: EditText
    private lateinit var etPasswordLogin: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvForgotPassword: TextView
    private lateinit var tvRegister: TextView

    private var failedLoginAttempts = 0
    private val MAX_LOGIN_ATTEMPTS = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        etEmailLogin = findViewById(R.id.etEmailLogin)
        etPasswordLogin = findViewById(R.id.etPasswordLogin)
        btnLogin = findViewById(R.id.btnLogin)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        tvRegister = findViewById(R.id.tvRegister)

        // Set click listener for login button
        btnLogin.setOnClickListener {
            validateLogin()
        }

        // Set click listener for forgot password
        tvForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }

        // Set click listener to redirect to register page
        tvRegister.setOnClickListener {
            redirectToRegister()
        }
    }

    private fun validateLogin() {
        // Get input values
        val email = etEmailLogin.text.toString().trim()
        val password = etPasswordLogin.text.toString().trim()

        // Validate email and password
        if (TextUtils.isEmpty(email)) {
            showToast("Please enter an email address")
            return
        } else if (!isValidEmail(email)) {
            showToast("Please enter a valid email address")
            return
        }

        if (TextUtils.isEmpty(password)) {
            showToast("Please enter a password")
            return
        }

        // Retrieve saved user data from SharedPreferences
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val savedEmail = sharedPref.getString("emailAddress", null)
        val savedPassword = sharedPref.getString("password", null)

        // Check if entered email and password match the saved data
        if (email == savedEmail && password == savedPassword) {
            showToast("Login successful")
            resetFailedLoginAttempts() // Reset failed attempts on successful login
            redirectToHome()
        } else {
            failedLoginAttempts++
            showToast("Invalid email or password")

            if (failedLoginAttempts >= MAX_LOGIN_ATTEMPTS) {
                disableLoginTemporarily()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun resetFailedLoginAttempts() {
        failedLoginAttempts = 0
    }

    private fun disableLoginTemporarily() {
        btnLogin.isEnabled = false
        showToast("Too many failed attempts. Try again in 30 seconds.")

        // Disable the login button for 30 seconds
        btnLogin.postDelayed({
            btnLogin.isEnabled = true
            showToast("You can try logging in again.")
        }, 30000) // 30 seconds delay
    }

    private fun redirectToHome() {
        // Redirect to the HomeActivity
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // Close LoginActivity
    }

    private fun redirectToRegister() {
        // Redirect to the RegisterActivity
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish() // Close LoginActivity
    }

    private fun showForgotPasswordDialog() {
        // Create a dialog to enter the email for password reset
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Reset Password")

        // Set up the input
        val input = EditText(this)
        input.hint = "Enter your email address"
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("Reset") { dialog, _ ->
            val email = input.text.toString().trim()
            if (TextUtils.isEmpty(email)) {
                showToast("Please enter an email address")
            } else if (!isValidEmail(email)) {
                showToast("Please enter a valid email address")
            } else {
                resetPassword(email)
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun resetPassword(email: String) {
        // Check if the email matches the registered email
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val savedEmail = sharedPref.getString("emailAddress", null)

        if (email == savedEmail) {
            // Allow the user to reset the password (for now, just show a success toast)
            showToast("Password reset instructions sent to your email")
        } else {
            showToast("Email not found")
        }
    }
}

