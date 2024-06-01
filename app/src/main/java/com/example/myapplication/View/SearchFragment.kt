package com.example.myapplication.View

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import com.example.myapplication.Repository.DatabaseHelper
import android.Manifest
import android.content.pm.PackageManager
import com.google.android.material.snackbar.Snackbar


class SearchFragment : Fragment(), AdapterSearch.OnItemClickListener {

    private lateinit var categoryButtons: List<Button>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterSearch
    private lateinit var searchView: SearchView
    private val REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 123

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = view.findViewById(R.id.rv_search)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        searchView = view.findViewById(R.id.idSV)

        categoryButtons = listOf(
            view.findViewById(R.id.btn_category1),
            view.findViewById(R.id.btn_category2),
            view.findViewById(R.id.btn_category3),
            view.findViewById(R.id.btn_category4),
            view.findViewById(R.id.btn_category5),
            view.findViewById(R.id.btn_category6),
            view.findViewById(R.id.btn_category7),
            view.findViewById(R.id.btn_category8),
            view.findViewById(R.id.btn_category9),
            view.findViewById(R.id.btn_category10),
            view.findViewById(R.id.btn_category11),
            view.findViewById(R.id.btn_category12),
            view.findViewById(R.id.btn_category13),
            view.findViewById(R.id.btn_category14),
            view.findViewById(R.id.btn_category15),
        )

        adapter = AdapterSearch(emptyList(), this)
        recyclerView.adapter = adapter
        setupSearchView()
        setupCategoryButtonListeners()
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            // Request the permission
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_READ_EXTERNAL_STORAGE_PERMISSION)
        } else {
            // Permission already granted, proceed with loading images
            loadAllProducts() // Or wherever you are loading images
        }
        return view
    }

    private fun setupCategoryButtonListeners() {
        for (button in categoryButtons) {
            button.setOnClickListener {
                val category = button.text.toString()
                if (category != adapter.currentCategory) {
                    // Only load products if the selected category is different from the current one
                    loadProductsByCategory(category)
                    adapter.currentCategory = category
                }
            }
        }
    }
    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }


    private fun loadProductsByCategory(category: String) {
        val databaseHelper = DatabaseHelper(requireContext())
        val categoryProductsCursor = databaseHelper.getProductsByCategory(category)

        val categoryProductsList = mutableListOf<Products>()

        categoryProductsCursor?.use { cursor ->
            val idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)
            val imageIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE)
            val nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)
            val descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION)
            val quantityIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY)
            val priceIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE)
            val price_promotionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PROMOTION_PRICE)
            val isLikedIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IS_LIKED)

            while (cursor.moveToNext()) {
                val id = cursor.getInt(idIndex)
                val imageUriString = cursor.getString(imageIndex)
                val imageUri = imageUriString?.let { Uri.parse(it) }
                val name = cursor.getString(nameIndex)
                val description = cursor.getString(descriptionIndex)
                val quantity = cursor.getInt(quantityIndex)
                val price = cursor.getDouble(priceIndex)
                val price_promotion = cursor.getDouble(price_promotionIndex)
                val isLiked = cursor.getInt(isLikedIndex) == 1

                val product = Products(id, imageUri, name, description, quantity, price,price_promotion,0, false, isLiked)
                categoryProductsList.add(product)
            }
        }

        adapter.updateDataCategory(categoryProductsList)
        adapter.currentCategory = category
    }
    private fun loadAllProducts() {
        val databaseHelper = DatabaseHelper(requireContext())
        val allProductsCursor = databaseHelper.getInfoProducts()

        val allProductsList = mutableListOf<Products>()

        allProductsCursor?.use { cursor ->
            val idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)
            val imageIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE)
            val nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)
            val descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION)
            val quantityIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY)
            val priceIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE)
            val price_discountIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PROMOTION_PRICE)
            val isLikedIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IS_LIKED)

            while (cursor.moveToNext()) {
                val id = cursor.getInt(idIndex)
                val imageUriString = cursor.getString(imageIndex)
                val imageUri = imageUriString?.let { Uri.parse(it) }
                val name = cursor.getString(nameIndex)
                val description = cursor.getString(descriptionIndex)
                val quantity = cursor.getInt(quantityIndex)
                val price = cursor.getDouble(priceIndex)
                val price_discount = cursor.getDouble(price_discountIndex)

                val isLiked = cursor.getInt(isLikedIndex) == 1

                val product = Products(id, imageUri, name, description, quantity, price, price_discount,0, false, isLiked)
                allProductsList.add(product)
            }
        }

        adapter.updateDataCategory(allProductsList)
        recyclerView.adapter = adapter
    }


    override fun onItemClick(product: Products) {
        val bundle = Bundle().apply {
            putParcelable("product", product)
        }
        val infoProductsFragment = InfoProductsFragment().apply {
            arguments = bundle
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.content_info_product, infoProductsFragment)
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

    override fun onAddProductClicked(product: Products) {
        val fragment = BottomSheetProductDetailFragment.newInstance(product)
        fragment.show(parentFragmentManager, "productDetail")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadAllProducts()
            } else {
                Snackbar.make(
                    requireView(),
                    "Permission required to access images",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }


}