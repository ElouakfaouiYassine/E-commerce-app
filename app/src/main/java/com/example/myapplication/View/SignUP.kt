package com.example.myapplication.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import com.example.myapplication.R
import com.example.myapplication.Repository.DataBaseUser
import com.example.myapplication.databinding.ActivitySingUpBinding
import com.google.android.material.snackbar.Snackbar

class SignUP : AppCompatActivity() {
    lateinit var binding: ActivitySingUpBinding
    lateinit var databaseUser: DataBaseUser
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseUser = DataBaseUser(this)
        binding.btnSignup.setOnClickListener{
            val signupusernam = binding.usernamSignup.text.toString()
            val signupemail = binding.emailSignup.text.toString()
            val signupphon = binding.tellSignup.text.toString()
            val signuppassword = binding.passwordSignup.text.toString()
            val signuppasswordconfirm = binding.passwordSignupVirefay.text.toString()
            if (signupusernam.isEmpty()){
                binding.usernamSignup.error = "The field is empty"
                binding.emailSignup.error = "The field is empty"
                binding.tellSignup.error = "The field is empty"
                binding.passwordSignup.error = "The field is empty"
                binding.passwordSignupVirefay.error = "The field is empty"
            }else if (!isValidName(signupusernam)){
                binding.usernamSignup.error = "The field is not valid"
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
                if (signupusernam.isNotEmpty() || signupemail.isNotEmpty() || signupphon.isNotEmpty() || signuppassword.isNotEmpty() || signuppasswordconfirm.isNotEmpty()){
                    signupDatabase(signupusernam, signupemail, signupphon, signuppassword)
                }else{
                    var snack = Snackbar.make(binding.root, "signup failed", Snackbar.LENGTH_LONG)
                    snack.show()
                }
            }
        }
        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        binding.showPasswordIcon.setOnClickListener {
            togglePasswordVisibility(binding.passwordSignup, binding.showPasswordIcon)
        }
        binding.showVerificationPasswordIcon.setOnClickListener {
            togglePasswordVisibility(binding.passwordSignupVirefay, binding.showVerificationPasswordIcon)
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
    private fun togglePasswordVerifyVisibility(editText: EditText, showPasswordIcon: ImageView) {
        if (editText.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            showPasswordIcon.setImageResource(R.drawable.visible)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            showPasswordIcon.setImageResource(R.drawable.invisible)
        }
        editText.setSelection(editText.text.length)
    }
    fun signupDatabase(username: String, email: String, phone: String, password: String) {
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