package com.example.myapplication.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Currency


class PanierFragment : Fragment(), AdapterPanier.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterPanier
    private lateinit var productList: MutableList<Products>
    private lateinit var tvTotalPrice: TextView
    private val baseUrl = "http://192.168.43.164/e-commerce%20app%20mobile%20back"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_panier, container, false)

        recyclerView = view.findViewById(R.id.rv_panier)
        tvTotalPrice = view.findViewById(R.id.tv_total_price)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        productList = mutableListOf()
        adapter = AdapterPanier(productList, { product -> removeProductFromCart(product) }, this)
        recyclerView.adapter = adapter

        loadProductsFromCart()

        return view
    }

    private fun loadProductsFromCart() {
        val url = "http://192.168.43.164/e-commerce%20app%20mobile%20back/get_cart_products.php"

        val requestQueue = Volley.newRequestQueue(requireContext())
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                productList.clear()
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val id = jsonObject.getInt("id")
                    val imageUri = jsonObject.getString("image")
                    val name = jsonObject.getString("name")
                    val description = jsonObject.getString("description")
                    val quantity = jsonObject.getInt("quantity")
                    val price = jsonObject.getDouble("price")
                    val price_promotion = jsonObject.getDouble("promotion_price")

                    val product = Products(id, imageUri, name, description, quantity, price, price_promotion)
                    productList.add(product)
                }
                adapter.notifyDataSetChanged()
                updateTotalPrice()
            },
            { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )

        requestQueue.add(jsonArrayRequest)
    }

    private fun updateTotalPrice() {
        val totalPrice = productList.sumByDouble { it.price * it.quantity }
        tvTotalPrice.text = "Total: ${formatCurrency(totalPrice)}"
    }

    private fun formatCurrency(amount: Double): String {
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.currency = Currency.getInstance("MAD")
        return currencyFormat.format(amount)
    }

    private fun removeProductFromCart(product: Products) {
        val url = "$baseUrl/remove_from_cart.php"
        val params = mapOf("id" to product.id.toString())

        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                Log.d("RemoveProductResponse", response)  // Log the raw response
                try {
                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getBoolean("success")
                    if (success) {
                        productList.remove(product)
                        adapter.notifyDataSetChanged()
                        updateTotalPrice()
                        Toast.makeText(requireContext(), "Product removed from cart", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to remove product from cart", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(requireContext(), "JSON Parsing error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                return params
            }
        }

        Volley.newRequestQueue(requireContext()).add(stringRequest)
    }


    override fun onItemClick(product: Products) {
        val bundle = Bundle().apply {
            putParcelable("product", product)
        }
        val infoProductsFragment = InfoProductsFragment().apply {
            arguments = bundle
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, infoProductsFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onProductImageClicked(product: Products) {
        val bundle = Bundle().apply {
            putParcelable("product", product)
        }
        val infoProductsFragment = InfoProductsFragment().apply {
            arguments = bundle
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, infoProductsFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onItemSelectionChanged() {
        updateTotalPrice()
    }
}

