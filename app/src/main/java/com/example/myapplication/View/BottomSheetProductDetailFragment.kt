package com.example.myapplication.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import com.example.myapplication.Repository.DataBasePanier
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class BottomSheetProductDetailFragment : BottomSheetDialogFragment() {

    private lateinit var product: Products

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        product = arguments?.getParcelable("product") ?: throw IllegalStateException("Product must not be null")

        view.findViewById<Button>(R.id.increaseQuantity).setOnClickListener {
            val quantityTextView: TextView = view.findViewById(R.id.quantityText)
            var quantity = quantityTextView.text.toString().toInt()
            if (quantity < product.quantity) {
                quantity++
                quantityTextView.text = quantity.toString()
            }
        }
        view.findViewById<Button>(R.id.decreaseQuantity).setOnClickListener {
            val quantityTextView: TextView = view.findViewById(R.id.quantityText)
            var quantity = quantityTextView.text.toString().toInt()
            if (quantity > 1) {
                quantity--
                quantityTextView.text = quantity.toString()
            }
        }

        setupViews(view)
        setupAddToCartButton(view)
    }

    private fun setupViews(view: View) {
        val productName: TextView = view.findViewById(R.id.productName)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val productDiscountPrice: TextView = view.findViewById(R.id.productDiscountPrice)
        val productDiscription: TextView = view.findViewById(R.id.productDiscriptiton)
        val productImage: ImageView = view.findViewById(R.id.productImage)

        productName.text = product.name
        productPrice.text = getString(R.string.price_format, product.price)
        productDiscountPrice.text = getString(R.string.price_format, product.price_promotion)
        productDiscription.text = product.description
        /*productImage.setImageURI(product.image_product)*/
    }

    private fun setupAddToCartButton(view: View) {
        val addToCartButton: Button = view.findViewById(R.id.addToCartButton)
        addToCartButton.setOnClickListener {
            val quantityText: TextView = view.findViewById(R.id.quantityText)
            val quantity = quantityText.text.toString().toInt()

            if (quantity <= product.quantity) {
                addToCart(product, quantity)
            } else {
                Snackbar.make(view, "Insufficient stock", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun addToCart(product: Products, quantity: Int) {
        val dbPanier = DataBasePanier(requireContext())
        val result = dbPanier.addToCart(product, quantity)
        if (result == -1L) {
            Snackbar.make(requireView(), "Failed to add product to cart or insufficient stock.", Snackbar.LENGTH_LONG).show()
        } else {
            Snackbar.make(requireView(), "Product added to cart successfully.", Snackbar.LENGTH_LONG).show()
        }
    }

    companion object {
        fun newInstance(product: Products): BottomSheetProductDetailFragment {
            val args = Bundle()
            args.putParcelable("product", product)
            val fragment = BottomSheetProductDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
