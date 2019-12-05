package com.example.minimoneybox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.minimoneybox.databinding.ActivityLoginBinding

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            btnSignIn.setOnClickListener {
                animation.playAnimation()
            }
        }
    }
}
