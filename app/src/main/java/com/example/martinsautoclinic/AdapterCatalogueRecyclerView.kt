package com.example.martinsautoclinic

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.martinsautoclinic.databinding.CatalogueEachItemBinding

class AdapterCatalogueRecyclerView(private val items: MutableList<dataClassServicesObjects>,private val clickListener: ((String) -> Unit)? = null ): RecyclerView.Adapter<AdapterCatalogueRecyclerView.MyViewHolder>() {

    inner class MyViewHolder(private val binding: CatalogueEachItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: dataClassServicesObjects) {

            if(item.servicePrice.isEmpty()){
                binding.textViewName.setTextAppearance(itemView.context, R.style.CustomRobotoBold)
                binding.textViewName.text = item.serviceName
                binding.textViewPrice.text = item.servicePrice
            }else {
                binding.textViewName.setTextAppearance(itemView.context, R.style.CustomRobotoRegular)
                binding.textViewName.text = item.serviceName
                binding.textViewPrice.text = "R" + item.servicePrice
            }

            itemView.setOnClickListener {
                clickListener?.invoke(item.serviceName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CatalogueEachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateServices(newServices: List<dataClassServicesObjects>) {
        items.clear()
        items.addAll(newServices)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        val movedItem = items[fromPosition]
        items.removeAt(fromPosition)
        items.add(toPosition, movedItem)
        notifyItemMoved(fromPosition, toPosition)
    }
}
