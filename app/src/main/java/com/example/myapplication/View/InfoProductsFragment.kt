package com.example.myapplication.View

import android.content.Context
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
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.myapplication.Repository.DataBasePanier
import com.example.myapplication.Repository.DatabaseHelper
import com.example.myapplication.ViewModel.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.NumberFormat
import java.util.Currency

class InfoProductsFragment : Fragment() {
    private var isLiked = false
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = arguments?.getParcelable<Products>("product")
        product?.let {
            displayProductInfo(it)
            updateLikeButtonUI()
        }
        view.findViewById<ImageView>(R.id.info_product_Favorite).setOnClickListener {
            toggleLikeStatus()
        }
        view.findViewById<Button>(R.id.btn_add_to_card).setOnClickListener {
            product?.let { product ->
                val bottomSheetFragment = BottomSheetProductDetailFragment.newInstance(product)
                bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
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
        val favoriteImageView = view?.findViewById<ImageView>(R.id.info_product_Favorite)
        favoriteImageView?.setImageResource(
            if (isLiked) R.drawable.icon_favorite else R.drawable.icon_nonfavorite
        )
    }

    private fun toggleLikeStatus() {
        isLiked = !isLiked // Toggle liked status
        val product = arguments?.getParcelable<Products>("product")
        product?.let {
            if (isLiked) {
                val userId = getUserId() // Retrieve the user ID
                if (userId == -1) {
                    Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_LONG).show()
                    return
                }
                addProductToFavorites(it.id, userId) // Pass the product ID and user ID
            }
        }
        updateLikeButtonUI()
    }

    private fun addProductToFavorites(productId: Int, userId: Int) {
        val url = "http://192.168.43.164/e-commerce%20app%20mobile%20back/add_to_favorites.php"
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                Log.d("Server Response", response)
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getBoolean("success")
                    if (success) {
                        Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_LONG).show()
                    } else {
                        val message = jsonObject.optString("message", "Unknown error")
                        Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.d("favorite exception", "Error: ${e.message}")
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Log.d("add favorite", "Error: ${error.message}")
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["product_id"] = productId.toString()
                params["user_id"] = userId.toString() // Use the retrieved user ID
                Log.d("Params", "Product ID: $productId, User ID: $userId")
                return params
            }
        }

        Volley.newRequestQueue(requireContext()).add(stringRequest)
    }

    private fun getUserId(): Int {
        val sharedPref = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("id", -1) // Use the correct key here
    }


    private fun displayProductInfo(product: Products) {
        val baseUrl = "http://192.168.43.164/e-commerce%20app%20mobile%20back/"
        val imageUrl = baseUrl + product.image
        Log.d("ImageURL", imageUrl) // Log the full URL for debugging
        val productImage = view?.findViewById<ImageView>(R.id.info_product)
        if (productImage != null) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.background_error)
                .error(R.drawable.background_error)
                .into(productImage)
        }

        view?.findViewById<TextView>(R.id.info_tv_namProduct)?.text = product.name
        view?.findViewById<TextView>(R.id.info_tv_descriptionProduct)?.text = product.description

        val priceTextView = view?.findViewById<TextView>(R.id.info_tv_priceProduct)
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.currency = Currency.getInstance("MAD")
        val formattedPrice = currencyFormat.format(product.price)
        priceTextView?.text = formattedPrice

        val promotion_priceTextView = view?.findViewById<TextView>(R.id.info_tv_priceDiscountProduct)
        val formattedPricePromotion = currencyFormat.format(product.price_promotion)
        promotion_priceTextView?.text = formattedPricePromotion
    }
}

