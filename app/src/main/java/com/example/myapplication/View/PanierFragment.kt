package com.example.myapplication.View

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import com.example.myapplication.Repository.DataBasePanier
import java.text.NumberFormat
import java.util.Currency


class PanierFragment : Fragment(), AdapterPanier.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterPanier
    private lateinit var panierDbHelper: DataBasePanier
    private lateinit var productList: MutableList<Products>
    private lateinit var tvTotalPrice: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_panier, container, false)

        recyclerView = view.findViewById(R.id.rv_panier)
        tvTotalPrice = view.findViewById(R.id.tv_total_price)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        panierDbHelper = DataBasePanier(requireContext())

        productList = mutableListOf()

        adapter = AdapterPanier(productList, { product -> removeProductFromCart(product) }, this)

        recyclerView.adapter = adapter

        loadProductsFromCart()

        return view
    }

    private fun loadProductsFromCart() {
        val cursor: Cursor? = panierDbHelper.getAllProductsInCart()

        productList.clear() // Clear the existing list before adding items from the database

        cursor?.use { c ->
            // Check if the cursor contains the required columns
            val idIndex = c.getColumnIndex(DataBasePanier.COLUMN_ID)
            val imageIndex = c.getColumnIndex(DataBasePanier.COLUMN_IMAGE)
            val nameIndex = c.getColumnIndex(DataBasePanier.COLUMN_NAME)
            val descriptionIndex = c.getColumnIndex(DataBasePanier.COLUMN_DESCRIPTION)
            val quantityIndex = c.getColumnIndex(DataBasePanier.COLUMN_QUANTITY_ORDER)
            val priceIndex = c.getColumnIndex(DataBasePanier.COLUMN_PRICE)
            val price_DiscountIndex = c.getColumnIndex(DataBasePanier.COLUMN_PROMOTION_PRICE)

            while (c.moveToNext()) {
                // Check if the column indices are valid
                if (idIndex == -1 || imageIndex == -1 || nameIndex == -1 ||
                    descriptionIndex == -1 || quantityIndex == -1 || priceIndex == -1 || price_DiscountIndex == -1) {
                    // Log an error or handle the missing columns
                    continue
                }

                val id = c.getInt(idIndex)
                val imageUri = c.getString(imageIndex)
                val name = c.getString(nameIndex)
                val description = c.getString(descriptionIndex)
                val quantity = c.getInt(quantityIndex)
                val price = c.getDouble(priceIndex)
                val price_promotion = c.getDouble(price_DiscountIndex)

                val product = Products(id, imageUri, name, description, quantity, price, price_promotion)
                productList.add(product)
            }
        }
        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged()
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        val totalPrice = productList.sumByDouble { it.price_Product * it.quantity_Product }
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.currency = Currency.getInstance("MAD")
        val formattedTotalPrice = currencyFormat.format(totalPrice)
        tvTotalPrice.text = "Total: $formattedTotalPrice"
    }
    private fun removeProductFromCart(product: Products) {
        val deletedRows = panierDbHelper.removeFromCart(product.nam_Product)
        if (deletedRows > 0) {
            productList.remove(product)
            adapter.notifyDataSetChanged()
            updateTotalPrice()
            Toast.makeText(requireContext(), "Product removed from cart", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Failed to remove product from cart", Toast.LENGTH_SHORT).show()
        }
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
