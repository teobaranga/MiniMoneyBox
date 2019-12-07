package com.example.minimoneybox.ui.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.minimoneybox.R
import com.example.minimoneybox.databinding.ActivityAccountBinding
import org.koin.android.viewmodel.ext.android.viewModel

const val EXTRA_PRODUCT_ID = "productId"

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding

    private val viewModel: AccountViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val productId = intent.getLongExtra(EXTRA_PRODUCT_ID, -1)
        if (productId != -1L) {
            viewModel.loadProduct(productId)
        }
    }
}
