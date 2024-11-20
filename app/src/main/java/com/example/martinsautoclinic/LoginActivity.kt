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
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmailLogin: EditText
    private lateinit var etPasswordLogin: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvForgotPassword: TextView
    private lateinit var tvRegister: TextView

    private var failedLoginAttempts = 0
    private val MAX_LOGIN_ATTEMPTS = 3

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        etEmailLogin = findViewById(R.id.etEmailLogin)
        etPasswordLogin = findViewById(R.id.etPasswordLogin)
        btnLogin = findViewById(R.id.btnLogin)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        tvRegister = findViewById(R.id.tvRegister)

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().reference

        // Set click listener for login button
        btnLogin.setOnClickListener {
            validateLogin()
        }

        tvRegister.setOnClickListener {
            // Redirect to the registration activity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Forgot password click listener
        tvForgotPassword.setOnClickListener {
            val email = etEmailLogin.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Use Firebase Auth to send a password reset email
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email sent to reset password", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorMessage = task.exception?.localizedMessage ?: "Failed to send reset email"
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun validateLogin() {
        val email = etEmailLogin.text.toString().trim()
        val password = etPasswordLogin.text.toString().trim()

        // Validate input
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

        // Replace periods in email with commas for Firebase key compatibility
        val formattedEmailKey = email.replace(".", ",")

        // Check if the email is in "Employees"
        databaseReference.child("employees").child(formattedEmailKey).get().addOnCompleteListener { employeeTask ->
            if (employeeTask.isSuccessful && employeeTask.result.exists()) {
                val dbPassword = employeeTask.result.child("password").getValue(String::class.java)
                if (dbPassword == password) {
                    // Redirect to admin dashboard
                    showToast("Welcome, Admin!")
                    resetFailedLoginAttempts()
                    redirectToAdminDashboard()
                } else {
                    failedLoginAttempts++
                    showToast("Invalid password for Admin")
                    if (failedLoginAttempts >= MAX_LOGIN_ATTEMPTS) {
                        disableLoginTemporarily()
                    }
                }
            } else {
                // Check if the email is in "Users"
                databaseReference.child("users").child(formattedEmailKey).get().addOnCompleteListener { userTask ->
                    if (userTask.isSuccessful && userTask.result.exists()) {
                        val dbPassword = userTask.result.child("password").getValue(String::class.java)
                        if (dbPassword == password) {
                            // Redirect to home page
                            showToast("Welcome, User!")
                            resetFailedLoginAttempts()
                            redirectToHomePage()
                        } else {
                            failedLoginAttempts++
                            showToast("Invalid password for User")
                            if (failedLoginAttempts >= MAX_LOGIN_ATTEMPTS) {
                                disableLoginTemporarily()
                            }
                        }
                    } else {
                        showToast("Email not found in Employees or Users")
                    }
                }
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
        btnLogin.postDelayed({
            btnLogin.isEnabled = true
            showToast("You can try logging in again.")
        }, 30000)
    }

    private fun redirectToAdminDashboard() {
        val intent = Intent(this, AdminDashboard::class.java)
        startActivity(intent)
        finish()
    }

    private fun redirectToHomePage() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}

