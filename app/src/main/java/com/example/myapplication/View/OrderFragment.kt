package com.example.myapplication.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.OrderItem
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import com.example.myapplication.Repository.DataBaseOrder
import com.example.myapplication.ViewModel.SharedViewModel


class OrderFragment : Fragment(),  AdapterOrder.OnItemClickListener{
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterOrder
    private lateinit var databaseOrder: DataBaseOrder
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseOrder = DataBaseOrder(requireContext())
        val orderList = databaseOrder.getAllOrders()

        recyclerView = view.findViewById(R.id.rv_order)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set this fragment as the listener for item click events
        adapter = AdapterOrder(orderList, this)
        recyclerView.adapter = adapter

        // Observe changes in the selected product
        sharedViewModel.getSelectedProduct().observe(viewLifecycleOwner, { product ->
            // Update UI with selected product
            val orderItem = OrderItem(
                id = 0, // Set a temporary id, you may need to change this
                image = product.image?.toString() ?: "", // Assuming Products has an image_product property
                name = product.name,
                description = product.description,
                price = product.price.toDouble(), // Convert price to Double
                price_promotion = product.price_promotion.toDouble()
            )
            val productList = listOf(orderItem)
            adapter.updateData(productList)
            databaseOrder.insertOrder(orderItem)
        })
    }
    override fun onItemClick(product: OrderItem) {
        // Handle item click
    }
}







