package com.example.myapplication.View

import android.R
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts

import com.example.myapplication.Repository.DatabaseHelper
import com.example.myapplication.databinding.ActivityAddPrductsBinding
import com.google.android.material.snackbar.Snackbar

class AddProductsActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddPrductsBinding
    lateinit var databaseProducts: DatabaseHelper
    private val categories = arrayOf("Phone", "TV", "Home", "Watch", "Gardin", "Accessories", "Motorcycle", "Shoes", "Sports", "Games", "Kids", "Tools & Industrial", "Electronics", "Man's", "Woman's")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPrductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseProducts = DatabaseHelper(this)

        val adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, categories)
        binding.spCategory.adapter = adapter
        var imageContract = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                binding.AddProductImage.setImageURI(uri)
                binding.AddProductImage.tag = uri
            }

        binding.AddProductImage.setOnClickListener {
            imageContract.launch(arrayOf("image/*"))
        }

        binding.btnLanchProduct.setOnClickListener {
            val addimageproduct = binding.AddProductImage
            val addnameproduct = binding.AddProductName.text.toString()
            val adddescriptionproduct = binding.AddProductDescription.text.toString()
            val addquantityproduct = binding.addProductQuantity.text.toString()
            val addpriceproduct = binding.addProductPrice.text.toString()
            val addpromotion_price = binding.addProductPromotionPrice.text.toString()
            val spcatery = binding.spCategory.selectedItem.toString()
            val imageUri: Uri? = addimageproduct.tag as Uri?

            if (imageUri == null){
                Snackbar.make(binding.root, "Image not found", Snackbar.LENGTH_LONG).show()
            }else if (addnameproduct.isEmpty()){
                binding.AddProductName.error = "The field is empty"
                binding.AddProductDescription.error = "The field is empty"
                binding.addProductQuantity.error = "The field is empty"
                binding.addProductPrice.error = "The field is empty"
                binding.addProductPromotionPrice.error = "The field is empty"
            }else if (!isValidNameDescriptionn(addnameproduct)){
                binding.AddProductName.error = "The field is not valid"
            }else if (addnameproduct.isEmpty() || adddescriptionproduct.isEmpty()){
                binding.AddProductDescription.error = "The field is empty"
                binding.addProductQuantity.error = "The field is empty"
                binding.addProductPrice.error = "The field is empty"
                binding.addProductPromotionPrice.error = "The field is empty"
            }else if (!isValidNameDescriptionn(addnameproduct) || !isValidNameDescriptionn(adddescriptionproduct)){
                binding.AddProductDescription.error = "The field is not valid"
            }else if (addnameproduct.isEmpty() || adddescriptionproduct.isEmpty() || addquantityproduct.isEmpty()){
                binding.addProductQuantity.error = "The field is empty"
                binding.addProductPrice.error = "The field is empty"
                binding.addProductPromotionPrice.error = "The field is empty"
            }else if (!isValidNameDescriptionn(addnameproduct) || !isValidNameDescriptionn(adddescriptionproduct) || !isValidPraceQuantity(addquantityproduct)){
                binding.addProductQuantity.error = "The field is not valid"
            }else if (addnameproduct.isEmpty() || adddescriptionproduct.isEmpty() || addquantityproduct.isEmpty() || addpriceproduct.isEmpty()){
                binding.addProductPrice.error = "The field is empty"
                binding.addProductPromotionPrice.error = "The field is empty"
            }else if (!isValidNameDescriptionn(addnameproduct) || !isValidNameDescriptionn(adddescriptionproduct) || !isValidPraceQuantity(addquantityproduct) || !isValidPraceQuantity(addpriceproduct)){
                binding.addProductPrice.error = "The field is not valid"
            }else if (addnameproduct.isEmpty() || adddescriptionproduct.isEmpty() || addquantityproduct.isEmpty() || addpriceproduct.isEmpty() || addpromotion_price.isEmpty()){
                binding.addProductPromotionPrice.error = "The field is empty"
            }else if (!isValidNameDescriptionn(addnameproduct) || !isValidNameDescriptionn(adddescriptionproduct) || !isValidPraceQuantity(addquantityproduct) || !isValidPraceQuantity(addpriceproduct) || !isValidPraceQuantity(addpromotion_price)){
                binding.addProductPromotionPrice.error = "The field is not valid"
            }else {
                if (imageUri != null){
                    productsDatabase(
                        imageUri,
                        addnameproduct,
                        adddescriptionproduct,
                        addquantityproduct,
                        addpriceproduct,
                        addpromotion_price,
                        spcatery
                    )
                }else {
                    Snackbar.make(binding.root, "Product error", Snackbar.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun productsDatabase(
        imageUri: Uri,
        name: String,
        description: String,
        quantity: String,
        price: String,
        promotion_Prace: String,
        category: String,
        ) {
        val insertRowId =
            databaseProducts.insertProducts(imageUri.toString(), name, description, quantity, price, promotion_Prace, category)
        if (insertRowId != -1L) {
            Snackbar.make(binding.root, "Product added successfully", Snackbar.LENGTH_LONG).show()
            // You can add additional actions if needed
        } else {
            Snackbar.make(binding.root, "Failed to add product", Snackbar.LENGTH_LONG).show()
        }
    }
    private fun isValidPraceQuantity(degits: String?): Boolean {
        val degitsRegex = "^[0-9]*$"
        val pattern = Regex(degitsRegex)
        return degits != null && pattern.matches(degits)
    }
    private fun isValidNameDescriptionn(name: String?): Boolean {
        val nameDescriptionRegex = "^[A-Za-z0-9 ]*$"
        val pattern = Regex(nameDescriptionRegex)
        return name != null && pattern.matches(name)
    }
}
