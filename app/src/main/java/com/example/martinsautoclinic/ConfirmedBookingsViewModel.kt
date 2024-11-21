package com.example.martinsautoclinic


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ChildEventListener

class ConfirmedBookingsViewModel : ViewModel() {

    private val databaseReference = FirebaseDatabase.getInstance().reference
    private val bookingsRef = databaseReference.child("bookings")
    private val _bookingList = MutableLiveData<List<Booking>>()
    val bookingList: LiveData<List<Booking>> = _bookingList

    private val bookingData = mutableListOf<Booking>()

    init {
        fetchBookingsFromFirebase()
    }

    private fun fetchBookingsFromFirebase() {
        bookingsRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val booking = snapshot.getValue(Booking::class.java)
                booking?.let {
                    bookingData.add(it)
                    _bookingList.value = bookingData
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Handle changes to existing bookings, if necessary
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Handle removal of bookings, if necessary
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Handle movements, if necessary
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors if Firebase fetch fails
            }
        })
    }

    fun addBooking(booking: Booking) {
        val newBookingRef = bookingsRef.push()
        newBookingRef.setValue(booking)
    }

    fun getBookings(): LiveData<List<Booking>> {
        return _bookingList
    }
}
