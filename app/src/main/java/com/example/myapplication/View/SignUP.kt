package com.example.myapplication.View

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.databinding.ActivitySingUpBinding
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject

class SignUP : AppCompatActivity() {
        lateinit var binding: ActivitySingUpBinding
        /*lateinit var databaseUser: DataBaseUser*/
        lateinit var sharedPreferences: SharedPreferences
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivitySingUpBinding.inflate(layoutInflater)
            setContentView(binding.root)
            /*databaseUser = DataBaseUser(this)*/
            binding.btnSignup.setOnClickListener{
                val signupusernam = binding.usernameSignup.text.toString()
                val signupemail = binding.emailSignup.text.toString()
                val signupphon = binding.tellSignup.text.toString()
                val signuppassword = binding.passwordSignup.text.toString()
                val signuppasswordconfirm = binding.passwordSignupVirefay.text.toString()
                if (signupusernam.isEmpty()){
                    binding.usernameSignup.error = "The field is empty"
                    binding.emailSignup.error = "The field is empty"
                    binding.tellSignup.error = "The field is empty"
                    binding.passwordSignup.error = "The field is empty"
                    binding.passwordSignupVirefay.error = "The field is empty"
                }else if (!isValidName(signupusernam)){
                    binding.usernameSignup.error = "The field is not valid"
                }else if (signupusernam.isEmpty() || signupemail.isEmpty()){
                    binding.emailSignup.error = "The field is empty"
                    binding.tellSignup.error = "The field is empty"
                    binding.passwordSignup.error = "The field is empty"
                    binding.passwordSignupVirefay.error = "The field is empty"
                }else if (!isValidName(signupusernam) || !isValidEmail(signupemail)) {
                    binding.emailSignup.error = "The field is not valid"
                }else if (signupusernam.isEmpty() || signupemail.isEmpty() || signupphon.isEmpty()){
                    binding.tellSignup.error = "The field is empty"
                    binding.passwordSignup.error = "The field is empty"
                    binding.passwordSignupVirefay.error = "The field is empty"
                }else if (!isValidName(signupusernam) || !isValidEmail(signupemail) || !isValidPhoneNumber(signupphon)) {
                    binding.tellSignup.error = "The field is empty"
                }else if (signupusernam.isEmpty() || signupemail.isEmpty() || signupphon.isEmpty() || signuppassword.isEmpty()){
                    binding.passwordSignup.error = "The field is empty"
                    binding.passwordSignupVirefay.error = "The field is empty"
                }else if (!isValidName(signupusernam) || !isValidEmail(signupemail) || !isValidPhoneNumber(signupphon) || !isValidPassword(signuppassword)) {
                    binding.passwordSignup.error = "The field is not valid"
                }else if (signupusernam.isEmpty() || signupemail.isEmpty() || signupphon.isEmpty() || signuppassword.isEmpty() || signuppasswordconfirm.isEmpty()){
                    binding.passwordSignupVirefay.error = "The field is empty"
                } else if (signuppassword != signuppasswordconfirm){
                    binding.passwordSignup.error = "The field is not valid verify your password"
                    binding.passwordSignupVirefay.error = "The field is not valid verify your Confirm password"
                }else {
                    signupUser(signupusernam, signupemail, signupphon, signuppassword)
                    /*if (signupusernam.isNotEmpty() || signupemail.isNotEmpty() || signupphon.isNotEmpty() || signuppassword.isNotEmpty() || signuppasswordconfirm.isNotEmpty()){
                        *//*signupDatabase(signupusernam, signupemail, signupphon, signuppassword)*//*
                    }else{
                        var snack = Snackbar.make(binding.root, "signup failed", Snackbar.LENGTH_LONG)
                        snack.show()
                    }*/
                }
            }
            binding.tvLogin.setOnClickListener {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }
        }

        private fun signupUser(username: String, email: String, phone: String, password: String) {
            val queue = Volley.newRequestQueue(this)
            val url = "http://192.168.43.164/e-commerce%20app%20mobile%20back/signup.php"

            val stringRequest = object : StringRequest(
                Request.Method.POST,
                url,
                { response ->
                    Log.d("Response", response) // Log the raw response
                    try {
                        val jsonResponse = JSONObject(response)
                        val success = jsonResponse.getBoolean("success")
                        if (success) {
                            Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        Log.e("JSONError", "JSON parsing error: ${e.message}")
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    Log.e("NetworkError", "Error: ${error.message}", error)
                    Toast.makeText(this, "Network error: ${error.message}", Toast.LENGTH_SHORT).show()
                }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["username"] = username
                    params["email"] = email
                    params["phone"] = phone
                    params["password"] = password
                    params["isAdmin"] =
                        if (username == "Admin" && password == "Admin.@123") "1" else "0"
                    return params
                }
            }
            queue.add(stringRequest)
        }
        /*fun signupDatabase(username: String, email: String, phone: String, password: String) {
            val insertRowId = databaseUser.insertUser(username, email, phone, password, false)
            if (insertRowId != -1L) {
                Snackbar.make(binding.root, "signup successful", Snackbar.LENGTH_LONG).show()
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            } else {
                Snackbar.make(binding.root, "signup failed", Snackbar.LENGTH_LONG).show()
            }
        }
    */

        fun isValidEmail(email: String?): Boolean {
            val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
            val pattern = Regex(emailRegex)
            return email != null && pattern.matches(email)
        }
        private fun isValidName(name: String?): Boolean {
            val nameRegex = "^[A-Z][a-z]*$"
            val pattern = Regex(nameRegex)
            return name != null && pattern.matches(name)
        }
        private fun isValidPhoneNumber(phoneNumber: String?): Boolean {
            val phoneRegex = "^[0-9]{10}$"
            val pattern = Regex(phoneRegex)
            return phoneNumber != null && pattern.matches(phoneNumber)
        }
        private fun isValidPassword(password: String?): Boolean {
            val passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$"
            val pattern = Regex(passwordRegex)
            return password != null && pattern.matches(password)
        }

    }