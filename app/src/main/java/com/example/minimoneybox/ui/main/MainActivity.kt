package com.example.minimoneybox.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.minimoneybox.R
import com.example.minimoneybox.databinding.ActivityMainBinding
import com.example.minimoneybox.ui.account.AccountActivity
import com.example.minimoneybox.ui.account.EXTRA_PRODUCT_ID
import com.example.minimoneybox.ui.login.LoginActivity
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        binding.viewModel = mainViewModel

        mainViewModel.currentUser.observe(this, Observer { user ->
            if (user == null) {
                // The user has signed out or there is some problem with the user data, go back to the login screen
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        })

        setupViews()
    }

    private fun setupViews() {
        binding.accounts.adapter = AccountsAdapter(click = {
            // Launch the Account details screen and request to display the selected product
            val intent = Intent(this, AccountActivity::class.java)
            intent.putExtra(EXTRA_PRODUCT_ID, it.id)
            startActivity(intent)
        })
    }
}
