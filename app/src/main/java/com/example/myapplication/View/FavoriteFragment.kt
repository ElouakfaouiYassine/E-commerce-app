package com.example.myapplication.View

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.myapplication.Repository.DatabaseHelper
import org.json.JSONArray
import org.json.JSONException
import java.lang.reflect.Method

class FavoriteFragment : Fragment(), AdapterFavorite.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var favoriteAdapter: AdapterFavorite

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_favorite, container, false)
        recyclerView = view.findViewById(R.id.rv_favorite)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchFavoriteProducts()
    }

    private fun fetchFavoriteProducts() {
        val userId = 1 // Replace with the current user's ID
        val url = "http://192.168.43.164/e-commerce%20app%20mobile%20back/get_favorites.php"

        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                Log.d("Response", "Server Response: $response")
                try {
                    val jsonArray = JSONArray(response)
                    val favoriteProductsList = mutableListOf<Products>()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val product = Products(
                            jsonObject.getInt("id"),
                            jsonObject.getString("image"),
                            jsonObject.getString("name"),
                            jsonObject.getString("description"),
                            jsonObject.optInt("quantity", 0),
                            jsonObject.optDouble("price", 0.0),
                            jsonObject.optDouble("price_promotion", 0.0)
                        )
                        favoriteProductsList.add(product)
                    }
                    favoriteAdapter = AdapterFavorite(favoriteProductsList, this)
                    recyclerView.adapter = favoriteAdapter
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Log.d("fetch favorites", "Error: ${error.message}")
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["user_id"] = userId.toString()
                return params
            }
        }

        Volley.newRequestQueue(requireContext()).add(stringRequest)
    }



    override fun onItemClick(product: Products) {
        // Handle item click
    }

    override fun onProductImageClicked(product: Products) {
        // Handle image click
    }
}
