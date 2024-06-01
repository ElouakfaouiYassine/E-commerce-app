package com.example.myapplication.View

import android.content.Intent
import android.database.Cursor
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import com.example.myapplication.Repository.DataBasePanier
import com.example.myapplication.Repository.DatabaseHelper
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment(), MyAdapter.OnItemClickListener {
    lateinit var dbHelper: DatabaseHelper
    lateinit var newList: ArrayList<Products>
    lateinit var recyclerView: RecyclerView
    lateinit var panierDbHelper: DataBasePanier
    lateinit var adapter: MyAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_home)
        dbHelper = DatabaseHelper(requireContext())
        panierDbHelper = DataBasePanier(requireContext())
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        displayProducts()
    }

    fun displayProducts() {
        var newcursor: Cursor? = dbHelper.getInfoProducts()
        newList = ArrayList()

        newcursor?.let {
            val imageIndex = it.getColumnIndex(DatabaseHelper.COLUMN_IMAGE)
            val nameIndex = it.getColumnIndex(DatabaseHelper.COLUMN_NAME)
            val descriptionIndex = it.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION)
            val quantityIndex = it.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY)
            val priceIndex = it.getColumnIndex(DatabaseHelper.COLUMN_PRICE)
            val priceDiscountIndex = it.getColumnIndex(DatabaseHelper.COLUMN_PROMOTION_PRICE)

            while (it.moveToNext()) {
                val imageUriString = if (imageIndex != -1) it.getString(imageIndex) else ""
                val name = if (nameIndex != -1) it.getString(nameIndex) else ""
                val description = if (descriptionIndex != -1) it.getString(descriptionIndex) else ""
                val quantity = if (quantityIndex != -1) it.getInt(quantityIndex) else 0
                val price = if (priceIndex != -1) it.getDouble(priceIndex) else 0.0
                val priceDiscount = if (priceDiscountIndex != -1) it.getDouble(priceDiscountIndex) else 0.0
                val imageUri = Uri.parse(imageUriString)


                val isLiked = dbHelper.isProductLiked(name)
                val isInCart = panierDbHelper.isProductInCart(name)
                val product = Products(imageUri, name, description, quantity , price, priceDiscount, isInCart, isLiked)
                newList.add(product)
            }
            it.close()
        }

        adapter = MyAdapter(newList, this)
        recyclerView.adapter = adapter
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

        override fun onAddProductClicked(product: Products) {
            val fragment = BottomSheetProductDetailFragment.newInstance(product)
            fragment.show(parentFragmentManager, "productDetail")
        }
    /*override fun onAddProductClicked(product: Products) {
        product.isInCart = true
        adapter.notifyDataSetChanged()

        addToCart(product)
    }*/

    private fun removeFromCart(productName: String) {
        val result = panierDbHelper.removeFromCart(productName)
        if (result != -1) {
            view?.let { rootView ->
                Snackbar.make(rootView, "Product removed from cart", Snackbar.LENGTH_LONG).show()
            }

            val updatedList = newList.map { if (it.nam_Product == productName) it.copy(isInCart = false) else it }
            (recyclerView.adapter as? MyAdapter)?.updateData(updatedList)
        } else {
            view?.let { rootView ->
                Snackbar.make(rootView, "Failed to remove product from cart", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onDeleteProductClicked(product: Products) {
        removeFromCart(product.nam_Product)
    }
}