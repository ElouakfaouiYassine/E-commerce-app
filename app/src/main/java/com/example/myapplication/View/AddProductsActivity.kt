package com.example.myapplication.View

import android.R
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityAddPrductsBinding
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class AddProductsActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddPrductsBinding
    private val categories = arrayOf("Phone", "TV", "Home", "Watch", "Gardin", "Accessories", "Motorcycle", "Shoes", "Sports", "Games", "Kids", "Tools & Industrial", "Electronics", "Man's", "Woman's")
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPrductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        queue = Volley.newRequestQueue(this)
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, categories)
        binding.spCategory.adapter = adapter
        val imageContract = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
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

            when {
                imageUri == null -> {
                    Snackbar.make(binding.root, "Image not found", Snackbar.LENGTH_LONG).show()
                }
                addnameproduct.isEmpty() -> {
                    binding.AddProductName.error = "The field is empty"
                    binding.AddProductDescription.error = "The field is empty"
                    binding.addProductQuantity.error = "The field is empty"
                    binding.addProductPrice.error = "The field is empty"
                    binding.addProductPromotionPrice.error = "The field is empty"
                }
                addnameproduct.isEmpty() || adddescriptionproduct.isEmpty() -> {
                    binding.AddProductDescription.error = "The field is empty"
                    binding.addProductQuantity.error = "The field is empty"
                    binding.addProductPrice.error = "The field is empty"
                    binding.addProductPromotionPrice.error = "The field is empty"
                }
                addnameproduct.isEmpty() || adddescriptionproduct.isEmpty() || addquantityproduct.isEmpty() -> {
                    binding.addProductQuantity.error = "The field is empty"
                    binding.addProductPrice.error = "The field is empty"
                    binding.addProductPromotionPrice.error = "The field is empty"
                }
                !isValidPraceQuantity(addquantityproduct) -> {
                    binding.addProductQuantity.error = "The field is not valid"
                }
                addnameproduct.isEmpty() || adddescriptionproduct.isEmpty() || addquantityproduct.isEmpty() || addpriceproduct.isEmpty() -> {
                    binding.addProductPrice.error = "The field is empty"
                    binding.addProductPromotionPrice.error = "The field is empty"
                }
                !isValidPraceQuantity(addquantityproduct) || !isValidPraceQuantity(addpriceproduct) -> {
                    binding.addProductPrice.error = "The field is not valid"
                }
                addnameproduct.isEmpty() || adddescriptionproduct.isEmpty() || addquantityproduct.isEmpty() || addpriceproduct.isEmpty() || addpromotion_price.isEmpty() -> {
                    binding.addProductPromotionPrice.error = "The field is empty"
                }
                !isValidPraceQuantity(addquantityproduct) || !isValidPraceQuantity(addpriceproduct) || !isValidPraceQuantity(addpromotion_price) -> {
                    binding.addProductPromotionPrice.error = "The field is not valid"
                }
                else -> {
                    val bitmap = (addimageproduct.drawable as BitmapDrawable).bitmap
                    val base64Image = bitmapToBase64(bitmap)
                    productsDatabase(
                        base64Image,
                        addnameproduct,
                        adddescriptionproduct,
                        addquantityproduct,
                        addpriceproduct,
                        addpromotion_price,
                        spcatery
                    )
                }
            }
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun productsDatabase(
        base64Image: String,
        name: String,
        description: String,
        quantity: String,
        price: String,
        promotion_Price: String,
        category: String,
    ) {
        val url = "http://192.168.43.164/e-commerce%20app%20mobile%20back/add_product.php"
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                Log.d("ServerResponse", response)
                try {
                    val response = JSONObject(response)
                    if (response.getBoolean("success")) {
                        val imagePath = response.getString("image_path")
                        val fullImageUrl = "http://192.168.43.164/e-commerce%20app%20mobile%20back/$imagePath"
                        Log.d("FullImageUrl", fullImageUrl)
                        Glide.with(this).load(fullImageUrl).into(binding.AddProductImage)
                    } else {
                        Snackbar.make(binding.root, "Failed to add product: ${response.getString("message")}", Snackbar.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Snackbar.make(binding.root, "Failed to parse server response", Snackbar.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Snackbar.make(binding.root, "Network error: ${error.message}", Snackbar.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["image"] = base64Image
                params["name"] = name
                params["description"] = description
                params["quantity"] = quantity
                params["price"] = price
                params["promotion_price"] = promotion_Price
                params["category"] = category
                return params
            }
        }
        queue.add(stringRequest)
    }
    private fun isValidPraceQuantity(degits: String?): Boolean {
        val degitsRegex = "^[0-9]*$"
        val pattern = Regex(degitsRegex)
        return degits != null && pattern.matches(degits)
    }
}