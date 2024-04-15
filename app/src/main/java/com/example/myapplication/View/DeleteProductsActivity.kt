package com.example.myapplication.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.Repository.DatabaseHelper
import com.example.myapplication.databinding.ActivityDeleteProductsBinding
import com.google.android.material.snackbar.Snackbar

class DeleteProductsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDeleteProductsBinding
    lateinit var databaseHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(this)
        binding.btnLanchProduct.setOnClickListener {
            val productNameToDelete = binding.DeletProductName.text.toString()
            val rowsAffected = databaseHelper.deleteProductByName(productNameToDelete)
            if (productNameToDelete.isEmpty()){
                binding.DeletProductName.error = "The field is empty"
            }else if (!isValidName(productNameToDelete)){
                binding.DeletProductName.error = "The field is not valid"
            }else{
                if (rowsAffected > 0) {
                    Snackbar.make(binding.root, "Product deleted successfully", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(binding.root, "Product not found or deletion failed", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun isValidName(name: String?): Boolean {
        val nameDescriptionRegex = "^[A-Za-z0-9 ]*$"
        val pattern = Regex(nameDescriptionRegex)
        return name != null && pattern.matches(name)
    }
}