package com.example.minimoneybox.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.example.minimoneybox.R
import com.example.minimoneybox.databinding.ActivityLoginBinding
import com.example.minimoneybox.ui.main.MainActivity

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

        loginViewModel.signedIn.observe(this, Observer { signedIn ->
            if (signedIn == true) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        })

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
