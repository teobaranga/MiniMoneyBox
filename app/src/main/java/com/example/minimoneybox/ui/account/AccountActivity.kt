package com.example.minimoneybox.ui.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.minimoneybox.R
import com.example.minimoneybox.databinding.ActivityAccountBinding

const val EXTRA_PRODUCT_ID = "productId"

class AccountActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding

    private lateinit var viewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[AccountViewModel::class.java]
        binding.viewModel = viewModel

        val productId = intent.getLongExtra(EXTRA_PRODUCT_ID, -1)
        if (productId != -1L) {
            viewModel.loadProduct(productId)
        }
    }
}
