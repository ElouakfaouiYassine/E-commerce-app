package com.example.myapplication.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import com.example.myapplication.Repository.DataBasePanier
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject

class BottomSheetProductDetailFragment : BottomSheetDialogFragment() {

    private lateinit var product: Products

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        product = arguments?.getParcelable("product") ?: throw IllegalStateException("Product must not be null")

        view.findViewById<Button>(R.id.increaseQuantity).setOnClickListener {
            val quantityTextView: TextView = view.findViewById(R.id.quantityText)
            var quantity = quantityTextView.text.toString().toInt()
            if (quantity < product.quantity) {
                quantity++
                quantityTextView.text = quantity.toString()
            }
        }
        view.findViewById<Button>(R.id.decreaseQuantity).setOnClickListener {
            val quantityTextView: TextView = view.findViewById(R.id.quantityText)
            var quantity = quantityTextView.text.toString().toInt()
            if (quantity > 1) {
                quantity--
                quantityTextView.text = quantity.toString()
            }
        }

        setupViews(view)
        setupAddToCartButton(view)
    }

    private fun setupViews(view: View) {
        val productName: TextView = view.findViewById(R.id.productName)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val productDiscountPrice: TextView = view.findViewById(R.id.productDiscountPrice)
        val productDescription: TextView = view.findViewById(R.id.productDescription)
        val productImage: ImageView = view.findViewById(R.id.productImage)

        productName.text = product.name
        productPrice.text = getString(R.string.price_format, product.price)
        productDiscountPrice.text = getString(R.string.price_format, product.price_promotion)
        productDescription.text = product.description

        val baseUrl = "http://192.168.43.164/e-commerce%20app%20mobile%20back/"
        val imageUrl = baseUrl + product.image

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.background_error)
            .error(R.drawable.background_error)
            .into(productImage)
    }

    private fun setupAddToCartButton(view: View) {
        val addToCartButton: Button = view.findViewById(R.id.addToCartButton)
        addToCartButton.setOnClickListener {
            val quantityText: TextView = view.findViewById(R.id.quantityText)
            val quantity = quantityText.text.toString().toInt()

            if (quantity <= product.quantity) {
                addToCart(product, quantity)
            } else {
                Snackbar.make(view, "Insufficient stock", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun addToCart(product: Products, quantity: Int) {
        val url = "http://192.168.43.164/e-commerce%20app%20mobile%20back/add_to_cart.php"

        val requestQueue = Volley.newRequestQueue(requireContext())

        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getBoolean("success")
                    val message = jsonObject.getString("message")
                    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Snackbar.make(requireView(), "JSON parsing error", Snackbar.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Snackbar.make(requireView(), "Volley error: ${error.message}", Snackbar.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["name"] = product.name
                params["description"] = product.description
                params["price"] = product.price.toString()
                params["promotion_price"] = product.price_promotion.toString()
                params["quantity"] = quantity.toString()
                params["image"] = product.image
                return params
            }
        }

        requestQueue.add(stringRequest)
    }

    companion object {
        fun newInstance(product: Products): BottomSheetProductDetailFragment {
            val args = Bundle()
            args.putParcelable("product", product)
            val fragment = BottomSheetProductDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}