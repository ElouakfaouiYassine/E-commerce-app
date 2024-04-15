package com.example.myapplication.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.Repository.DataBaseUser
import com.example.myapplication.databinding.ActivityDeleteProfileBinding
import com.example.myapplication.databinding.ActivityUpdateProfileBinding
import com.google.android.material.snackbar.Snackbar

class DeleteProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityDeleteProfileBinding
    lateinit var dataBaseUser: DataBaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataBaseUser = DataBaseUser(this)
        binding.btnDeleteUser.setOnClickListener {
            var deletUserEmail = binding.emailDelete.text.toString()
            var rowsAffected = dataBaseUser.deleteUserByName(deletUserEmail)
            if (deletUserEmail.isEmpty()){
                binding.emailDelete.error = "The field is empty"
            }else if (!isValidEmail(deletUserEmail)){
                binding.emailDelete.error = "The field is not valid"
            }else{
                if (rowsAffected > 0){

                    Snackbar.make(binding.root, "User deleted successfully", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(binding.root, "User not found or deletion failed", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
    fun isValidEmail(email: String?): Boolean {
        val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
        val pattern = Regex(emailRegex)
        return email != null && pattern.matches(email)
    }
}