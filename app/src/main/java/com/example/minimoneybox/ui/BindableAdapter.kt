package com.example.minimoneybox.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("data")
fun <T> setRecyclerViewData(recyclerView: RecyclerView, data: T?) {
    data?.let {
        @Suppress("UNCHECKED_CAST")
        (recyclerView.adapter as? BindableAdapter<T>)?.setData(it)
    }
}

interface BindableAdapter<T> {
    fun setData(data: T)
}
