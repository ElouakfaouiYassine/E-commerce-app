package com.example.myapplication.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import com.example.myapplication.Repository.DataBasePanier
import com.google.android.material.snackbar.Snackbar

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var product: Products

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)


        product = intent.getParcelableExtra("product") ?: throw IllegalStateException("Product must not be null")



        findViewById<Button>(R.id.increaseQuantity).setOnClickListener {
            val quantityTextView: TextView = findViewById(R.id.quantityText)
            var quantity = quantityTextView.text.toString().toInt()
            if (quantity < product.quantity) {
                quantity++
                quantityTextView.text = quantity.toString()
            }
        }
        findViewById<Button>(R.id.decreaseQuantity).setOnClickListener {
            val quantityTextView: TextView = findViewById(R.id.quantityText)
            var quantity = quantityTextView.text.toString().toInt()
            if (quantity > 1) {
                quantity--
                quantityTextView.text = quantity.toString()
            }
        }

        setupViews()
        setupAddToCartButton()
    }

    private fun setupViews() {
        val productName: TextView = findViewById(R.id.productName)
        val productPrice: TextView = findViewById(R.id.productPrice)
        val productDiscountPrice: TextView = findViewById(R.id.productDiscountPrice)
        val productDiscription: TextView = findViewById(R.id.productDiscriptiton)
        val productImage: ImageView = findViewById(R.id.productImage)


        productName.text = product.name

        productPrice.text = getString(R.string.price_format, product.price)
        productDiscountPrice.text = getString(R.string.price_format, product.price)
        productDiscription.text = product.description

        /*roductImage.setImageURI(product.image_product)*/


    }

    private fun setupAddToCartButton() {
        val addToCartButton: Button = findViewById(R.id.addToCartButton)
        addToCartButton.setOnClickListener {
            val quantityText: TextView = findViewById(R.id.quantityText)
            val quantity = quantityText.text.toString().toInt()

            if (quantity <= product.quantity) {
                addToCart(product, quantity)
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Insufficient stock", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun addToCart(product: Products, quantity: Int) {
        val dbPanier = DataBasePanier(this)
        val result = dbPanier.addToCart(product, quantity)
        if (result == -1L) {
            Snackbar.make(findViewById(android.R.id.content), "Failed to add product to cart or insufficient stock.", Snackbar.LENGTH_LONG).show()
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Product added to cart successfully.", Snackbar.LENGTH_LONG).show()
        }
    }

}