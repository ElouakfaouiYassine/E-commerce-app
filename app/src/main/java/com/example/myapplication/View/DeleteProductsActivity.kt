package com.example.myapplication.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.Repository.DatabaseHelper
import com.example.myapplication.databinding.ActivityDeleteProductsBinding
import com.google.android.material.snackbar.Snackbar

class DeleteProductsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDeleteProductsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLanchProduct.setOnClickListener {
            val productNameToDelete = binding.DeletProductName.text.toString()
            if (productNameToDelete.isEmpty()){
                binding.DeletProductName.error = "The field is empty"
            }else if (!isValidName(productNameToDelete)){
                binding.DeletProductName.error = "The field is not valid"
            }
        }
    }
    private fun isValidName(name: String?): Boolean {
        val nameDescriptionRegex = "^[A-Za-z0-9 ]*$"
        val pattern = Regex(nameDescriptionRegex)
        return name != null && pattern.matches(name)
    }
}