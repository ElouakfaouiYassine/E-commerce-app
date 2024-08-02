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
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.Repository.DataBaseUser
import com.example.myapplication.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject

class Login : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    /*lateinit var databaseUser: DataBaseUser*/
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*databaseUser = DataBaseUser(this)*/
        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

        binding.btnLogin.setOnClickListener {
            val loginUsername = binding.loginUsername.text.toString()
            val loginPassword = binding.loginPassword.text.toString()

            if (loginUsername.isEmpty() || loginPassword.isEmpty()) {
                if (loginUsername.isEmpty()) {
                    binding.loginUsername.error = "Username field is empty"
                }
                if (loginPassword.isEmpty()) {
                    binding.loginPassword.error = "Password field is empty"
                }
                return@setOnClickListener
            }

            if (!isValidName(loginUsername)) {
                binding.loginUsername.error = "Invalid username format"
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
    }


    fun loginDatabase(username:String, password:String) {
        val queue = Volley.newRequestQueue(this)
            val url = "http://192.168.43.164/e-commerce%20app%20mobile%20back/login.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST,
            url,
            { response ->
                val jsonResponse = JSONObject(response)
                if (jsonResponse.getBoolean("success")) {
                    Snackbar.make(binding.root, "Login successful", Snackbar.LENGTH_LONG).show()
                    sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                    sharedPreferences.edit().putBoolean("isAdmin", jsonResponse.getBoolean("isAdmin")).apply()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Snackbar.make(binding.root, "Login failed: ${jsonResponse.getString("message")}", Snackbar.LENGTH_LONG).show()
                }
            },
            { error ->
                Snackbar.make(binding.root, "Network error: ${error.message}", Snackbar.LENGTH_LONG).show()
            })
        {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                return params
            }
        }
        queue.add(stringRequest)
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