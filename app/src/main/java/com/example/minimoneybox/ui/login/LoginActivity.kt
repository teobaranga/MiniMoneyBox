package com.example.minimoneybox.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.example.minimoneybox.R
import com.example.minimoneybox.databinding.ActivityLoginBinding

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this

        loginViewModel = ViewModelProvider(this, AndroidViewModelFactory.getInstance(application))[LoginViewModel::class.java]
        binding.viewModel = loginViewModel

        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            btnSignIn.setOnClickListener {
                animation.playAnimation()

                loginViewModel.login()
            }
        }

    }
}
