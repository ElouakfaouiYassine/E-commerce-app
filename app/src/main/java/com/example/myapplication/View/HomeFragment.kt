package com.example.myapplication.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.Model.NetworkHome
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import com.example.myapplication.Repository.DataBasePanier
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment(), MyAdapter.OnItemClickListener {
    lateinit var newList: ArrayList<Products>
    lateinit var recyclerView: RecyclerView
    lateinit var panierDbHelper: DataBasePanier
    lateinit var adapter: MyAdapter
    private lateinit var networkHome: NetworkHome

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_home)
        networkHome = NetworkHome(requireContext())
        panierDbHelper = DataBasePanier(requireContext())
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        fetchProductsFromServer()
    }

    private fun fetchProductsFromServer() {
        networkHome.fetchProducts(
            onSuccess = { products ->
                newList = ArrayList(products)
                adapter = MyAdapter(newList, this)
                recyclerView.adapter = adapter
            },
            onError = { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            }
        )
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

    override fun onDeleteProductClicked(product: Products) {
        removeFromCart(product.nam_Product)
    }

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
}
