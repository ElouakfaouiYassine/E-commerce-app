package com.example.myapplication.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.Model.Products

class SharedViewModel : ViewModel() {
    private val selectedProduct = MutableLiveData<Products>()

    fun setSelectedProduct(product: Products) {
        selectedProduct.value = product
    }

    fun getSelectedProduct(): LiveData<Products> {
        return selectedProduct
    }
}