package com.example.myapplication.View

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import com.example.myapplication.Repository.DatabaseHelper

class FavoriteFragment : Fragment(), AdapterFavorite.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var favoriteAdapter: AdapterFavorite
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_favorite, container, false)
        recyclerView = view.findViewById(R.id.rv_favorite)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        databaseHelper = DatabaseHelper(requireContext())

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayFavoriteProducts()
    }

    private fun displayFavoriteProducts() {
        val favoriteProducts = getFavoriteProductsFromDB()
        favoriteAdapter = AdapterFavorite(favoriteProducts, this)
        recyclerView.adapter = favoriteAdapter
    }

    private fun getFavoriteProductsFromDB(): List<Products> {
        val favoriteProductsList = mutableListOf<Products>()
        val cursor = databaseHelper.getInfoProducts()
        cursor?.use {
            val columnIndexImage = it.getColumnIndex(DatabaseHelper.COLUMN_IMAGE)
            val columnIndexName = it.getColumnIndex(DatabaseHelper.COLUMN_NAME)
            val columnIndexDescription = it.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION)
            val columnIndexQuantity = it.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY)
            val columnIndexPrice = it.getColumnIndex(DatabaseHelper.COLUMN_PRICE)
            val columnIndexPricePromotion = it.getColumnIndex(DatabaseHelper.COLUMN_PROMOTION_PRICE)
            val columnIndexIsLiked = it.getColumnIndex(DatabaseHelper.COLUMN_IS_LIKED)
            while (it.moveToNext()) {
                val isLiked = it.getInt(columnIndexIsLiked) == 1
                if (isLiked) {
                    val imageUri = it.getString(columnIndexImage)
                    val name = it.getString(columnIndexName) ?: ""
                    val description = it.getString(columnIndexDescription) ?: ""
                    val quantity = it.getInt(columnIndexQuantity)
                    val price = it.getDouble(columnIndexPrice)
                    val price_promotion = it.getDouble(columnIndexPricePromotion)
                    val product = Products(0, imageUri, name, description, quantity, price, price_promotion, isLiked = isLiked)
                    favoriteProductsList.add(product)
                }
            }
        }
        return favoriteProductsList
    }

    override fun onItemClick(product: Products) {
        val bundle = Bundle().apply {
            putParcelable("product", product)
        }
        val infoProductsFragment = InfoProductsFragment().apply {
            arguments = bundle
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout , infoProductsFragment)
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
            .replace(R.id.frame_layout , infoProductsFragment)
            .addToBackStack(null)
            .commit()
    }

}