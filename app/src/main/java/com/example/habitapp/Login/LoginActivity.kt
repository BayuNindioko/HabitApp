package com.example.habitapp.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.example.habitapp.MainActivity
import com.example.habitapp.R
import com.example.habitapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //logic buat cek udah login atau belum, kalo udah pindah ke main
//        val currentUser = FirebaseAuth.getInstance().currentUser
//        if (currentUser != null) {
//
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }

        binding.loginButton.setOnClickListener {
            signIn()
        }

        binding.signupButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (checkForm()) {
            binding.progressBar.visibility = View.VISIBLE
            //Firebase auth logic taro sini

//            kalo udah selesai login ilangin progressbar pake
//            binding.progressBar.visibility = View.GONE
        }
    }

    private fun checkForm(): Boolean {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        var isValid = true

        when {
            email.isEmpty() -> {
                binding.emailEditTextLayout.error = getString(R.string.error_enter_email)
                isValid = false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emailEditTextLayout.error = getString(R.string.invalid_email)
                isValid = false
            }
            else -> {
                binding.emailEditTextLayout.error = null
            }
        }

        when {
            password.isEmpty() -> {
                binding.passwordEditTextLayout.error = getString(R.string.error_passrword)
                isValid = false
            }
            password.length < 5 -> {
                binding.passwordEditTextLayout.error = getString(R.string.password_leght)
                isValid = false
            }
            else -> {
                binding.passwordEditTextLayout.error = null
            }
        }

        return isValid
    }
}