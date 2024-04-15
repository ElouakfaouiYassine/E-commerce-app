package com.example.myapplication.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.marginBottom
import com.example.myapplication.R
import com.example.myapplication.Repository.DataBaseUser
import com.example.myapplication.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar

class Login : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var databaseUser: DataBaseUser
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseUser = DataBaseUser(this)
        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

        binding.btnLogin.setOnClickListener {
            val loginUsername = binding.loginUsernam.text.toString()
            val loginPassword = binding.loginPassword.text.toString()

            if (loginUsername.isEmpty() || loginPassword.isEmpty()) {
                if (loginUsername.isEmpty()) {
                    binding.loginUsernam.error = "Username field is empty"
                }
                if (loginPassword.isEmpty()) {
                    binding.loginPassword.error = "Password field is empty"
                }
                return@setOnClickListener
            }

            if (!isValidName(loginUsername)) {
                binding.loginUsernam.error = "Invalid username format"
                return@setOnClickListener
            }

            if (!isValidPassword(loginPassword)) {
                binding.loginPassword.error = "Invalid password format"
                return@setOnClickListener
            }

            loginDatabase(loginUsername, loginPassword)
        }
        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, SignUP::class.java)
            startActivity(intent)
        }
        binding.showPasswordIcon.setOnClickListener {
            togglePasswordVisibility(binding.loginPassword, binding.showPasswordIcon)
        }
    }


    private fun togglePasswordVisibility(editText: EditText, showPasswordIcon: ImageView) {
        if (editText.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            showPasswordIcon.setImageResource(R.drawable.visible)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            showPasswordIcon.setImageResource(R.drawable.invisible)
        }
        editText.setSelection(editText.text.length)
    }

    fun loginDatabase(username: String, password: String) {
        val userExist = databaseUser.readUser(username, password)
        if (userExist) {
            Snackbar.make(binding.root, "login successful", Snackbar.LENGTH_LONG).show()
            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

            // Check if the user is admin
            val isAdmin = username == "Admin" && password == "Admin.@123"
            sharedPreferences.edit().putBoolean("isAdmin", isAdmin).apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Snackbar.make(binding.root, "login failed", Snackbar.LENGTH_LONG).show()
        }
    }


    private fun isValidName(name: String?): Boolean {
        val nameRegex = "^[A-Z][a-z]*$"
        val pattern = Regex(nameRegex)
        return name != null && pattern.matches(name)
    }

    private fun isValidPassword(password: String?): Boolean {
        val passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$"
        val pattern = Regex(passwordRegex)
        return password != null && pattern.matches(password)
    }

}