package com.example.myapplication.View

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.Model.MultipartRequest
import com.example.myapplication.Model.VolleyMultipartRequest
import com.example.myapplication.Repository.DatabaseHelper
import com.example.myapplication.databinding.ActivityUpdateProductsBinding
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class UpdateProductsActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateProductsBinding

    /*lateinit var databaseHelper: DatabaseHelper*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*databaseHelper = DatabaseHelper(this)*/
        val imageContract = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            binding.UpdateProductImage.setImageURI(uri)
            binding.UpdateProductImage.tag = uri
        }

        binding.UpdateProductImage.setOnClickListener {
            imageContract.launch(arrayOf("image/*"))
        }

        binding.btnUpdateProductUP.setOnClickListener {
            val nameofproduct = binding.NameProductUpdate.text.toString()
            val newName = binding.UpdateProductName.text.toString()
            val newDescription = binding.UpdateProductDescription.text.toString()
            val newQuantity = binding.UpdateProductQuantity.text.toString()
            val newPrace = binding.UpdateProductPrice.text.toString()
            val newPricePromtion = binding.UpdateProductPricePromotion.text.toString()
            val imageUri: Uri? = binding.UpdateProductImage.tag as Uri?

            if (isValidForm(nameofproduct, newName, newDescription, newQuantity, newPrace, newPricePromtion)) {
                updateProduct(nameofproduct, imageUri, newName, newDescription, newQuantity, newPrace, newPricePromtion)
            } else {
                showValidationErrors()
            }
        }
    }

    private fun updateProduct(
        oldName: String,
        newImage: Uri?,
        newName: String,
        newDescription: String,
        newQuantity: String,
        newPrice: String,
        newPromotion: String
    ) {
        val requestQueue = Volley.newRequestQueue(this)
        val url = "http://192.168.43.164/e-commerce%20app%20mobile%20back/updateProduct.php"

        val multipartRequest = object : VolleyMultipartRequest(Request.Method.POST, url,
            Response.Listener { response ->
                try {
                    val jsonResponse = JSONObject(String(response.data))
                    if (jsonResponse.getBoolean("success")) {
                        Snackbar.make(binding.root, "Product updated successfully", Snackbar.LENGTH_LONG).show()
                    } else {
                        Snackbar.make(binding.root, "Update failed: ${jsonResponse.getString("message")}", Snackbar.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    Log.d("ServerResponseUpdate", "Error parsing response: ${e.message}")
                    Snackbar.make(binding.root, "Error parsing response: ${e.message}", Snackbar.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Snackbar.make(binding.root, "Update failed: ${error.message}", Snackbar.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["action"] = "updateProduct"
                params["oldName"] = oldName
                params["newName"] = newName
                params["newDescription"] = newDescription
                params["newQuantity"] = newQuantity
                params["newPrice"] = newPrice
                params["newPromotion"] = newPromotion
                return params
            }

            override fun getByteData(): Map<String, DataPart> {
                val params = HashMap<String, DataPart>()
                if (newImage != null) {
                    val inputStream = contentResolver.openInputStream(newImage)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    inputStream?.copyTo(byteArrayOutputStream)
                    params["newImage"] = DataPart(
                        "newImage.jpg",
                        byteArrayOutputStream.toByteArray(),
                        "image/jpeg"
                    )
                }
                return params
            }
        }

        requestQueue.add(multipartRequest)
    }

    private fun isValidForm(vararg fields: String): Boolean {
        return fields.all { it.isNotEmpty() }
    }

    private fun showValidationErrors() {
        binding.NameProductUpdate.error = "The field is empty"
        binding.UpdateProductName.error = "The field is empty"
        binding.UpdateProductDescription.error = "The field is empty"
        binding.UpdateProductQuantity.error = "The field is empty"
        binding.UpdateProductPrice.error = "The field is empty"
        binding.UpdateProductPricePromotion.error = "The field is empty"
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

