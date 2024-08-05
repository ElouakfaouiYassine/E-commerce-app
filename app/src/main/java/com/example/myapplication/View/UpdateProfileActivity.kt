package com.example.myapplication.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.Repository.DataBaseUser
import com.example.myapplication.databinding.ActivityUpdateProfileBinding
import com.google.android.material.snackbar.Snackbar

class UpdateProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateProfileBinding
    lateinit var dataBaseUser: DataBaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataBaseUser = DataBaseUser(this)

        binding.btnUpdateUser.setOnClickListener {
            val emailUserUpdate = binding.emailSignupUpdate.text.toString()
            val usernameUpdate = binding.usernamUpdate.text.toString()
            val emailUpdate = binding.emailUpdate.text.toString()
            val phoneNumberUpdate = binding.tellUpdate.text.toString()
            val passwordUpdate = binding.passwordUpdate.text.toString()
            val confirmPasswordUpdate = binding.confirmPasswordUpdate.text.toString()

            if (emailUserUpdate.isEmpty()){
                binding.emailSignupUpdate.error = "The field is empty"
                binding.usernamUpdate.error = "The field is empty"
                binding.emailUpdate.error = "The field is empty"
                binding.tellUpdate.error = "The field is empty"
                binding.passwordUpdate.error = "The field is empty"
                binding.confirmPasswordUpdate.error = "The field is empty"
            }else if (!isValidEmail(emailUserUpdate)){
                binding.emailSignupUpdate.error = "The field is not valid"
            }else if (emailUserUpdate.isEmpty() || usernameUpdate.isEmpty()){
                binding.usernamUpdate.error = "The field is empty"
                binding.emailUpdate.error = "The field is empty"
                binding.tellUpdate.error = "The field is empty"
                binding.passwordUpdate.error = "The field is empty"
                binding.confirmPasswordUpdate.error = "The field is empty"
            }else if (!isValidEmail(emailUserUpdate) || !isValidName(usernameUpdate)){
                binding.usernamUpdate.error = "The field is not valid"
            }else if (emailUserUpdate.isEmpty() || usernameUpdate.isEmpty() || emailUpdate.isEmpty()){
                binding.emailUpdate.error = "The field is empty"
                binding.tellUpdate.error = "The field is empty"
                binding.passwordUpdate.error = "The field is empty"
                binding.confirmPasswordUpdate.error = "The field is empty"
            }else if (!isValidEmail(emailUserUpdate) || !isValidName(usernameUpdate) || !isValidEmail(emailUpdate)) {
                binding.emailUpdate.error = "The field is not valid"
            }else if (emailUserUpdate.isEmpty() || usernameUpdate.isEmpty() || emailUpdate.isEmpty() || phoneNumberUpdate.isEmpty()){
                binding.tellUpdate.error = "The field is empty"
                binding.passwordUpdate.error = "The field is empty"
                binding.confirmPasswordUpdate.error = "The field is empty"
            }else if (!isValidEmail(emailUserUpdate) || !isValidName(usernameUpdate) || !isValidEmail(emailUpdate) || !isValidPhoneNumber(phoneNumberUpdate)){
                binding.tellUpdate.error = "The field is not valid"
            }else if (emailUserUpdate.isEmpty() || usernameUpdate.isEmpty() || emailUpdate.isEmpty() || phoneNumberUpdate.isEmpty() || passwordUpdate.isEmpty()){
                binding.passwordUpdate.error = "The field is empty"
                binding.confirmPasswordUpdate.error = "The field is empty"
            }else if (!isValidEmail(emailUserUpdate) || !isValidName(usernameUpdate) || !isValidEmail(emailUpdate) || !isValidPhoneNumber(phoneNumberUpdate) || !isValidPassword(confirmPasswordUpdate)){
                binding.confirmPasswordUpdate.error = "The field is not valid"
            }else if (emailUserUpdate.isEmpty() || usernameUpdate.isEmpty() || emailUpdate.isEmpty() || phoneNumberUpdate.isEmpty() || passwordUpdate.isEmpty() || confirmPasswordUpdate.isEmpty()){
                binding.confirmPasswordUpdate.error = "The field is empty"
            }else if (passwordUpdate != confirmPasswordUpdate){
                binding.passwordUpdate.error = "The field is not valid verify your password"
                binding.confirmPasswordUpdate.error = "The field is not valid verify your Confirm password"
            } else{
                val rowsAffected = dataBaseUser.updateUserByEmail(emailUserUpdate, usernameUpdate, emailUpdate, phoneNumberUpdate,passwordUpdate)
                if (rowsAffected > 0) {
                    Snackbar.make(binding.root, "User updated successfully", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(binding.root, "User not found or update failed", Snackbar.LENGTH_LONG).show()
                }
            }
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