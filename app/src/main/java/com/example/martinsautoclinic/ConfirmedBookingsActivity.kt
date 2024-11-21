package com.example.martinsautoclinic


import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class ConfirmedBookingsActivity : AppCompatActivity() {

    private val email: String = "customerName"
    private val date: String = "bookingDate"
    private val services: List<String> = listOf("serviceType")
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmed_bookings)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("appointments")

        // Booking container
        val bookingContainer = findViewById<LinearLayout>(R.id.booking_container)

        // Fetch data
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookingContainer.removeAllViews()
                for (appointmentSnapshot in snapshot.children) {
                    val appointment = appointmentSnapshot.getValue(ConfirmedBookingsActivity::class.java)

                    val bookingView = layoutInflater.inflate(R.layout.confirmed_bookings_item, null)
                    val customerName = bookingView.findViewById<TextView>(R.id.tv_customer_name)
                    val bookingDate = bookingView.findViewById<TextView>(R.id.tv_booking_date)
                    val serviceType = bookingView.findViewById<TextView>(R.id.tv_service_type)

                    customerName.text = "Customer: ${appointment?.email}"
                    bookingDate.text = "Date: ${appointment?.date}"
                    serviceType.text = "Services: ${appointment?.services?.joinToString(", ")}"

                    bookingContainer.addView(bookingView)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}
