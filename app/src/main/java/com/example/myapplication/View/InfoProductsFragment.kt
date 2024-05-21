package com.example.myapplication.View

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.example.myapplication.Repository.DataBasePanier
import com.example.myapplication.Repository.DatabaseHelper
import com.example.myapplication.ViewModel.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.text.NumberFormat

class InfoProductsFragment : Fragment() {
    private var dbHelper: DatabaseHelper? = null
    private var isLiked = false
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_info_products, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())
        val product = arguments?.getParcelable<Products>("product")
        product?.let {
            displayProductInfo(it)
            isLiked = dbHelper?.isProductLiked(it.nam_Product) ?: false
            updateLikeButtonUI()
        }
        view.findViewById<ImageView>(R.id.info_product_Favorite).setOnClickListener {
            toggleLikeStatus()
        }
        view.findViewById<Button>(R.id.btn_add_to_card).setOnClickListener {
            product?.let { product ->
                handleAddToCart(product)
            }
        }

        view.findViewById<Button>(R.id.btn_buy_now).setOnClickListener {
            val product = arguments?.getParcelable<Products>("product")
            product?.let {
                sharedViewModel.setSelectedProduct(it)
            }
            val paymentFragment = PaymentFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, paymentFragment)
                .addToBackStack(null)
                .commit()
        }


    }


    private fun updateLikeButtonUI() {
        // Update the UI of the like button based on the liked status
        val favoriteImageView = view?.findViewById<ImageView>(R.id.info_product_Favorite)
        favoriteImageView?.setImageResource(
            if (isLiked) R.drawable.icon_favorite else R.drawable.icon_nonfavorite
        )
    }
    private fun toggleLikeStatus() {
        isLiked = !isLiked // Toggle liked status
        updateProductLikeStatus(isLiked) // Update liked status in the database
        updateLikeButtonUI()
    }

    private fun updateProductLikeStatus(isLiked: Boolean) {
        val product = requireArguments().getParcelable<Products>("product")
        val productName = product?.nam_Product

        productName?.let { productName ->
            val rowsAffected = dbHelper?.updateProductLikeStatus(productName, isLiked)
            if (rowsAffected == null || rowsAffected == -1) {
                Log.e("UpdateStatus", "Failed to update like status for product: $productName")
            } else {
                Log.d("UpdateStatus", "Successfully updated like status for product: $productName")
            }
        } ?: Log.e("UpdateStatus", "Product name is null")
    }



    fun displayProductInfo(product: Products) {
        view?.apply {
            val imageView = findViewById<ImageView>(R.id.info_product)
            product.image_product?.let { uri ->
                try {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    imageView.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            findViewById<ImageView>(R.id.info_product).setImageURI(product.image_product)
            findViewById<TextView>(R.id.info_tv_namProduct).text = product.nam_Product
            findViewById<TextView>(R.id.info_tv_descriptionProduct).text = product.description_Product
            findViewById<TextView>(R.id.info_tv_quantityProduct).text = product.quantity_Product.toString()
            findViewById<TextView>(R.id.info_tv_priceProduct).text = product.price_Product.toString()
            findViewById<TextView>(R.id.info_tv_priceDiscountProduct).text = product.discount_Price_Product.toString()
            val formattedPrice = NumberFormat.getCurrencyInstance().format(product.price_Product)
            findViewById<TextView>(R.id.info_tv_priceProduct).text = formattedPrice
            val formattedPriceDiscount = NumberFormat.getCurrencyInstance().format(product.discount_Price_Product)
            findViewById<TextView>(R.id.info_tv_priceDiscountProduct).text = formattedPriceDiscount
        }
    }
    private fun handleAddToCart(product: Products) {
        val database = DataBasePanier(requireContext())
        if (database.isProductInCart(product.nam_Product)) {
            showSnackbar("Product already in cart")
        } else {
            openProductDetailActivity(product)
        }
    }

    private fun openProductDetailActivity(product: Products) {
        val intent = Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra("product", product)
        }
        startActivity(intent)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }
}