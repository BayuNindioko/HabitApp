package com.example.habitapp.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import com.example.habitapp.MainActivity
import com.example.habitapp.R
import com.example.habitapp.databinding.ActivityLoginBinding
import com.example.habitapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap
import android.widget.Toast

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.signupButton.setOnClickListener {
            signUp()
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("Sign Up")
        }
    }

    private fun signUp(){
        val name = binding.usernameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val confirmPassword = binding.confirmPasswordEditText.text.toString()

        if (checkForm()) {
            binding.progressBar3.visibility = View.VISIBLE

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val userUID = user?.uid

                    val db = FirebaseFirestore.getInstance()
                    val userDocument = HashMap<String, Any>()
                    userDocument["nama"] = name

                    if (userUID != null) {
                        db.collection("Users")
                            .document(userUID)
                            .set(userDocument)
                            .addOnSuccessListener {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this,
                                    "Gagal menyimpan data pengguna ke Firestore: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.progressBar3.visibility = View.GONE
                            }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Gagal membuat akun Firebase: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar3.visibility = View.GONE
                }
            }
        }
    }

    private fun checkForm(): Boolean {
        val name = binding.usernameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val confirmpassword = binding.confirmPasswordEditText.text.toString()
        var isValid = true

        when {
            name.isEmpty() -> {
                binding.usernameEditTextLayout.error = getString(R.string.error_enter_username)
                isValid = false
            }
            else -> {
                binding.usernameEditTextLayout.error = null
            }
        }

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

        when {
            confirmpassword.isEmpty() -> {
                binding.confirmPassword.error = getString(R.string.error_passrword)
                isValid = false
            }

            confirmpassword != password -> {
                binding.confirmPassword.error = getString(R.string.passwords_do_not_match)
                isValid = false
            }

            else -> {
                binding.confirmPassword.error = null
            }
        }

        return isValid
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}