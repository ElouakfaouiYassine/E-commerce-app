package com.example.myapplication.View

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapplication.Repository.DatabaseHelper
import com.example.myapplication.databinding.ActivityUpdateProductsBinding
import com.google.android.material.snackbar.Snackbar

class UpdateProductsActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateProductsBinding
    lateinit var databaseHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(this)
        var imageContract = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            binding.UpdateProductImage.setImageURI(uri)
            binding.UpdateProductImage.tag = uri
        }

        binding.UpdateProductImage.setOnClickListener {
            imageContract.launch(arrayOf("image/*"))
        }

        binding.btnUpdateProductUP.setOnClickListener {
            var nameofproduct = binding.NameProductUpdate.text.toString()
            var updateImage = binding.UpdateProductImage
            var newName = binding.UpdateProductName.text.toString()
            val newDescription = binding.UpdateProductDescription.text.toString()
            val newQuantity = binding.UpdateProductQuantity.text.toString()
            val newPrace = binding.UpdateProductPrice.text.toString()
            val newPricePromtion = binding.UpdateProductPricePromotion.text.toString()
            var imageUri: Uri? = updateImage.tag as Uri?

            if (nameofproduct.isEmpty()){
                binding.NameProductUpdate.error = "The field is empty"
                binding.UpdateProductName.error = "The field is empty"
                binding.UpdateProductDescription.error = "The field is empty"
                binding.UpdateProductQuantity.error = "The field is empty"
                binding.UpdateProductPrice.error = "The field is empty"
                binding.UpdateProductPricePromotion.error = "The field is empty"
            }else if(!isValidNameDescriptionn(nameofproduct)){
                binding.NameProductUpdate.error = "The field is not valid"
            }else if (nameofproduct.isEmpty() || newName.isEmpty()){
                binding.UpdateProductName.error = "The field is empty"
                binding.UpdateProductDescription.error = "The field is empty"
                binding.UpdateProductQuantity.error = "The field is empty"
                binding.UpdateProductPrice.error = "The field is empty"
                binding.UpdateProductPricePromotion.error = "The field is empty"
            }else if(!isValidNameDescriptionn(nameofproduct) || !isValidNameDescriptionn(newName)){
                binding.UpdateProductName.error = "The field is not valid"
            }else if (nameofproduct.isEmpty() || newName.isEmpty() || newDescription.isEmpty()){
                binding.UpdateProductDescription.error = "The field is empty"
                binding.UpdateProductQuantity.error = "The field is empty"
                binding.UpdateProductPrice.error = "The field is empty"
                binding.UpdateProductPricePromotion.error = "The field is empty"
            }else if(!isValidNameDescriptionn(nameofproduct) || !isValidNameDescriptionn(newName) || !isValidNameDescriptionn(newDescription)){
                binding.UpdateProductDescription.error = "The field is not valid"
            }else if (nameofproduct.isEmpty() || newName.isEmpty() || newDescription.isEmpty() || newQuantity.isEmpty()){
                binding.UpdateProductQuantity.error = "The field is empty"
                binding.UpdateProductPrice.error = "The field is empty"
                binding.UpdateProductPricePromotion.error = "The field is empty"
            }else if(!isValidNameDescriptionn(nameofproduct) || !isValidNameDescriptionn(newName) || !isValidNameDescriptionn(newDescription) || !isValidQuantityPrice(newQuantity)){
                binding.UpdateProductQuantity.error = "The field is not valid"
            }else if (nameofproduct.isEmpty() || newName.isEmpty() || newDescription.isEmpty() || newQuantity.isEmpty() || newPrace.isEmpty()){
                binding.UpdateProductPrice.error = "The field is empty"
                binding.UpdateProductPricePromotion.error = "The field is empty"
            }else if(!isValidNameDescriptionn(nameofproduct) || !isValidNameDescriptionn(newName) || !isValidNameDescriptionn(newDescription) || !isValidQuantityPrice(newQuantity) || !isValidQuantityPrice(newPrace)){
                binding.UpdateProductPrice.error = "The field is not valid"
            }else if (nameofproduct.isEmpty() || newName.isEmpty() || newDescription.isEmpty() || newQuantity.isEmpty() || newPrace.isEmpty() || newPricePromtion.isEmpty()){
                binding.UpdateProductPrice.error = "The field is empty"
                binding.UpdateProductPricePromotion.error = "The field is empty"
            }else if(!isValidNameDescriptionn(nameofproduct) || !isValidNameDescriptionn(newName) || !isValidNameDescriptionn(newDescription) || !isValidQuantityPrice(newQuantity) || !isValidQuantityPrice(newPrace) || !isValidQuantityPrice(newPricePromtion)){
                binding.UpdateProductPricePromotion.error = "The field is not valid"
            }else{
                val rowsAffected = databaseHelper.updateProductByName(nameofproduct, imageUri, newName, newDescription, newQuantity, newPrace, newPricePromtion)
                if (rowsAffected > 0) {
                    // Product updated successfully
                    Snackbar.make(binding.root, "Product updated successfully", Snackbar.LENGTH_LONG).show()
                } else {
                    // Product not found or update failed
                    Snackbar.make(binding.root, "Product not found or update failed", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun isValidNameDescriptionn(name: String?): Boolean {
        val nameDescriptionRegex = "^[A-Za-z0-9 ]*$"
        val pattern = Regex(nameDescriptionRegex)
        return name != null && pattern.matches(name)
    }

    private fun isValidQuantityPrice(phoneNumber: String?): Boolean {
        val phoneRegex = "^[0-9]*$"
        val pattern = Regex(phoneRegex)
        return phoneNumber != null && pattern.matches(phoneNumber)
    }
}
