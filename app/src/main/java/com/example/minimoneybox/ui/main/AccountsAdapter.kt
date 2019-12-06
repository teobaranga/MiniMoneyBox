package com.example.minimoneybox.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.minimoneybox.databinding.ItemAccountBinding
import com.example.minimoneybox.repo.model.Product
import com.example.minimoneybox.ui.BindableAdapter

class AccountsAdapter(val click: (product: Product) -> Unit) : RecyclerView.Adapter<AccountsAdapter.ViewHolder>(),
    BindableAdapter<List<Product>> {

    var products = emptyList<Product>()

    override fun setData(data: List<Product>) {
        products = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAccountBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])
    }

    inner class ViewHolder(private val binding: ItemAccountBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(product: Product) {
            binding.product = product
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            click(binding.product!!)
        }
    }
}
