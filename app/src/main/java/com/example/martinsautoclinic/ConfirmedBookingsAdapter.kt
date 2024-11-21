package com.example.martinsautoclinic


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.martinsautoclinic.databinding.ConfirmedBookingsItemBinding

class ConfirmedBookingsAdapter :
    ListAdapter<Booking, ConfirmedBookingsAdapter.BookingViewHolder>(BookingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ConfirmedBookingsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = getItem(position)
        holder.bind(booking)
    }

    inner class BookingViewHolder(private val binding: ConfirmedBookingsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(booking: Booking) {
            binding.tvCustomerName.text = "Customer: ${booking.customerName}"
            binding.tvBookingDate.text = "Date: ${booking.bookingDate}"
            binding.tvServiceType.text = "Service: ${booking.serviceType}"
        }
    }

    class BookingDiffCallback : DiffUtil.ItemCallback<Booking>() {
        override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem.bookingDate == newItem.bookingDate
        }

        override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem == newItem
        }
    }
}
