package com.example.myapplication.View

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.myapplication.Model.VolleyFileUploadRequest
import com.example.myapplication.Repository.DatabaseHelper
import com.example.myapplication.databinding.ActivityUpdateProductsBinding
import com.google.android.material.snackbar.Snackbar

class UpdateProductsActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateProductsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productId = intent.getStringExtra("PRODUCT_ID")
        val productName = intent.getStringExtra("PRODUCT_NAME")
        val productDescription = intent.getStringExtra("PRODUCT_DESCRIPTION")
        val productImage = intent.getStringExtra("PRODUCT_IMAGE")
        val productQuantity = intent.getStringExtra("PRODUCT_QUANTITY")
        val productPrice = intent.getStringExtra("PRODUCT_PRICE")
        val productPromotionPrice = intent.getStringExtra("PRODUCT_PRICE_PROMOTION")

        Log.d("UpdateProductsActivity", "Product ID: $productId")
        Log.d("UpdateProductsActivity", "Product Name: $productName")
        Log.d("UpdateProductsActivity", "Product Description: $productDescription")
        Log.d("UpdateProductsActivity", "Product Image: $productImage")
        Log.d("UpdateProductsActivity", "Product Quantity: $productQuantity")
        Log.d("UpdateProductsActivity", "Product Price: $productPrice")
        Log.d("UpdateProductsActivity", "Product Price Promotion: $productPromotionPrice")

        if (productId.isNullOrEmpty()) {
            Log.e("UpdateProductsActivity", "Product ID is missing.")
            finish() // Close activity if product ID is missing
            return
        }

        binding.UpdateProductName.setText(productName)
        binding.UpdateProductDescription.setText(productDescription)
        binding.UpdateProductQuantity.setText(productQuantity)
        binding.UpdateProductPrice.setText(productPrice)
        binding.UpdateProductPricePromotion.setText(productPromotionPrice)

        Glide.with(this)
            .load("http://192.168.43.164/e-commerce%20app%20mobile%20back/$productImage")
            .into(binding.UpdateProductImage)

        val imageContract = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.UpdateProductImage.setImageURI(uri)
                binding.UpdateProductImage.tag = uri
            }
        }

        binding.UpdateProductImage.setOnClickListener {
            imageContract.launch("image/*")
        }

        binding.btnUpdateProductUP.setOnClickListener {
            val newName = binding.UpdateProductName.text.toString()
            val newDescription = binding.UpdateProductDescription.text.toString()
            val newQuantity = binding.UpdateProductQuantity.text.toString()
            val newPrice = binding.UpdateProductPrice.text.toString()
            val newPricePromotion = binding.UpdateProductPricePromotion.text.toString()
            val imageUri: Uri? = binding.UpdateProductImage.tag as Uri?

            if (validateInput(newName, newDescription, newQuantity, newPrice, newPricePromotion)) {
                Log.d("UpdateProducttttt", "productId: $productId, newName: $newName, newDescription: $newDescription, newQuantity: $newQuantity, newPrice: $newPrice, newPricePromotion: $newPricePromotion, imageUri: $imageUri")
                updateProduct(productId, imageUri, newName, newDescription, newQuantity, newPrice, newPricePromotion)
            }
        }
    }

    private fun validateInput(vararg inputs: String): Boolean {
        inputs.forEach { input ->
            if (input.isEmpty() || (!isValidNameDescription(input) && !isValidQuantityPrice(input))) {
                Snackbar.make(binding.root, "Please fill all fields correctly", Snackbar.LENGTH_LONG).show()
                return false
            }
        }
        return true
    }

    private fun updateProduct(productId: String, imageUri: Uri?, newName: String, newDescription: String, newQuantity: String, newPrice: String, newPricePromotion: String) {
        val url = "http://192.168.43.164/e-commerce%20app%20mobile%20back/updateProduct.php"
        val request = object : VolleyFileUploadRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                val responseData = String(response.data)
                Log.d("ServerResponse", responseData)
                Snackbar.make(binding.root, "Product updated successfully", Snackbar.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->
                Log.e("VolleyError", "Error: ${error.message}")
                Snackbar.make(binding.root, "Error: ${error.message}", Snackbar.LENGTH_LONG).show()
            }
        ) {
            override fun getByteData(): Map<String, DataPart> {
                val params = HashMap<String, DataPart>()
                imageUri?.let {
                    val inputStream = contentResolver.openInputStream(it)
                    val imageBytes = inputStream?.readBytes()
                    if (imageBytes != null) {
                        params["image"] = DataPart("image.jpg", imageBytes, "image/jpg")
                    }
                }
                return params
            }

            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["product_id"] = productId
                params["new_name"] = newName
                params["new_description"] = newDescription
                params["new_quantity"] = newQuantity
                params["new_price"] = newPrice
                params["new_price_promotion"] = newPricePromotion

                Log.d("UpdateProductParams", params.toString())
                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }

    private fun isValidNameDescription(name: String?): Boolean {
        val nameDescriptionRegex = "^[A-Za-z0-9 ]*$"
        val pattern = Regex(nameDescriptionRegex)
        return name != null && pattern.matches(name)
    }

    private fun isValidQuantityPrice(quantityPrice: String?): Boolean {
        val quantityPriceRegex = "^[0-9]*$"
        val pattern = Regex(quantityPriceRegex)
        return quantityPrice != null && pattern.matches(quantityPrice)
    }
}
