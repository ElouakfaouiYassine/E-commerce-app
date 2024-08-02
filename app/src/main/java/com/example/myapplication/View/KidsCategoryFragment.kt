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


class KidsCategoryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var databaseHelper: DatabaseHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_kids_category, container, false)
        recyclerView = view.findViewById(R.id.rv_kids_category)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MyAdapter(mutableListOf(), object : MyAdapter.OnItemClickListener {
            override fun onItemClick(product: Products) {
                // Handle item click
            }

            override fun onAddProductClicked(product: Products) {
                // Handle add product click
            }

            override fun onDeleteProductClicked(product: Products) {
                // Handle delete product click
            }

            override fun onProductImageClicked(product: Products) {
                // Handle product image click
            }
        })
        recyclerView.adapter = adapter
        databaseHelper = DatabaseHelper(requireContext())
        loadkidsProducts()
        return view
    }

    private fun loadkidsProducts() {
        val kidsProductsCursor = databaseHelper.getProductsByCategory("Kids")
        val kidsProductsList = mutableListOf<Products>()

        kidsProductsCursor?.use { cursor ->
            val idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)
            val imageIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE)
            val nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)
            val descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION)
            val quantityIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY)
            val priceIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE)
            val pricePromotionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PROMOTION_PRICE)

            while (cursor.moveToNext()) {
                val id = cursor.getInt(idIndex)
                val imageUri = cursor.getString(imageIndex)
                val name = cursor.getString(nameIndex)
                val description = cursor.getString(descriptionIndex)
                val quantity = cursor.getInt(quantityIndex)
                val price = cursor.getDouble(priceIndex)
                val price_promotion = cursor.getDouble(pricePromotionIndex)
                val product = Products(id, imageUri, name, description, quantity, price, price_promotion)
                kidsProductsList.add(product)
            }
        }

        adapter.updateData(kidsProductsList)
        kidsProductsCursor?.close()
    }

}