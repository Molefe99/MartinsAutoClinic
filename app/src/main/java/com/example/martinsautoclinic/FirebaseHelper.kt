package com.example.martinsautoclinic

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.FirebaseDatabase


object FirebaseHelper {

    private val database = FirebaseDatabase.getInstance().reference
    private val bookingsRef = database.child("bookings")

    fun addBooking(booking: Booking) {
        val newBookingRef = bookingsRef.push()
        newBookingRef.setValue(booking)
    }

    fun getBookings(listener: ChildEventListener) {
        bookingsRef.addChildEventListener(listener)
    }
}
