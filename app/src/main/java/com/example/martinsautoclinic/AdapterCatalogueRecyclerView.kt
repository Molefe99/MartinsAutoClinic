package com.example.martinsautoclinic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.martinsautoclinic.databinding.CatalogueEachItemBinding

class AdapterCatalogueRecyclerView(private val items: MutableList<dataClassServicesObjects>,private val clickListener: ((String) -> Unit)? = null ): RecyclerView.Adapter<AdapterCatalogueRecyclerView.MyViewHolder>() {

    // ViewHolder class that holds reference to the item views using ViewBinding
    inner class MyViewHolder(private val binding: CatalogueEachItemBinding) : RecyclerView.ViewHolder(binding.root) {

        // Function to bind data to the TextView
        fun bind(item: dataClassServicesObjects) {
            // Bind the item to the textView in EachItemBinding

            if(item.servicePrice.isEmpty()){
                binding.textViewName.setTextAppearance(itemView.context, R.style.CustomRobotoBold)
                binding.textViewName.text = item.serviceName
                binding.textViewPrice.text = item.servicePrice
                //binding.textViewName.setTextAppearance(itemView.context,R.style.robotto)
            }else {
                binding.textViewName.setTextAppearance(itemView.context, R.style.CustomRobotoRegular)
                binding.textViewName.text = item.serviceName
                binding.textViewPrice.text = "R" + item.servicePrice
            }
            //binding.textViewPrice.text = item.second

            itemView.setOnClickListener {
                clickListener?.invoke(item.serviceName) // Call the listener with the item when clicked
            }
        }
    }

    // Inflate the layout and return the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Use EachItemBinding to inflate the layout for each item
        val binding = CatalogueEachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    // Bind the data to the ViewHolder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Call the bind function to set the data for each item
        holder.bind(items[position])
    }
    // Add a method to update the list and notify changes
    fun updateServices(newServices: List<dataClassServicesObjects>) {
        items.clear()
        items.addAll(newServices)
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
    // Return the total number of items
    override fun getItemCount(): Int {
        return items.size
    }
    // Method to handle item movement
    fun onItemMove(fromPosition: Int, toPosition: Int) {
        // Move the item in the list
        val movedItem = items[fromPosition]
        items.removeAt(fromPosition)
        items.add(toPosition, movedItem)
        notifyItemMoved(fromPosition, toPosition)
    }
}
