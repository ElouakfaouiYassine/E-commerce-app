package com.example.myapplication.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import java.text.NumberFormat
import java.util.Currency

class AdapterPanier(var list:List<Products>, private val deleteListener: (Products) -> Unit, var itemClick: OnItemClickListener) : RecyclerView.Adapter<AdapterPanier.PanierViewHolder>() {

    private val selectedProducts = mutableSetOf<Products>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PanierViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.info_rv_panier, parent, false)

        return PanierViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterPanier.PanierViewHolder, position: Int) {
        val currentProduct = list[position]
        holder.bind(currentProduct)
    }

    fun updateData(newList: List<Products>) {
        list = newList
        notifyDataSetChanged()
    }
    override fun getItemCount() = list.size

    interface OnItemClickListener {
        fun onItemClick(product: Products)
        fun onProductImageClicked(product: Products)
        fun onItemSelectionChanged()
    }
    inner class PanierViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkboxProduct: CheckBox = itemView.findViewById(R.id.checkbox_product)
        private val imageDeletePanier: ImageView = itemView.findViewById(R.id.icon_delete_Panier)
        private val imageviewInfo: ImageView = itemView.findViewById(R.id.image_info_panier)
        private val tvnameInfo: TextView = itemView.findViewById(R.id.info_tv_namP_panier)
        private val tvdescriptionInfo: TextView = itemView.findViewById(R.id.info_tv_description_panier)
        private val tvquantityInfo: TextView = itemView.findViewById(R.id.info_tv_quantity_panier)
        private val tvpriceInfo: TextView = itemView.findViewById(R.id.info_tv_priceP_panier)
        private val tvpriceDiscountInfo: TextView = itemView.findViewById(R.id.info_tv_priceDiscount_panier)

        init {

            itemView.setOnClickListener {
                itemClick.onItemClick(list[adapterPosition])
            }

            imageviewInfo.setOnClickListener {
                itemClick.onProductImageClicked(list[adapterPosition])
            }
            checkboxProduct.setOnCheckedChangeListener { _, isChecked ->
                val product = list[adapterPosition]
                if (isChecked) {
                    selectedProducts.add(product)
                } else {
                    selectedProducts.remove(product)
                }
                itemClick.onItemSelectionChanged()
            }

            imageDeletePanier.setOnClickListener {
                deleteListener(list[adapterPosition])
            }

        }
        fun bind(product: Products) {
            Glide.with(itemView.context).load(product.image).into(imageviewInfo)
            tvnameInfo.text = product.name
            tvdescriptionInfo.text = product.description
            tvquantityInfo.text = product.quantity.toString()
            tvpriceInfo.text = product.price.toString()
            tvpriceDiscountInfo.text = product.price_promotion.toString()
            val currencyFormat = NumberFormat.getCurrencyInstance()
            currencyFormat.currency = Currency.getInstance("MAD")
            val formattedPrice = currencyFormat.format(product.price)
            tvpriceInfo.text = formattedPrice

            val formattedPriceDiscount = currencyFormat.format(product.price_promotion)
            tvpriceDiscountInfo.text = formattedPriceDiscount

            checkboxProduct.setOnCheckedChangeListener { _, isChecked ->
                val product = list[adapterPosition]
                if (isChecked) {
                    selectedProducts.add(product)
                } else {
                    selectedProducts.remove(product)
                }
                itemClick.onItemSelectionChanged()
            }
        }
    }
    fun getSelectedProducts(): List<Products> {
        return selectedProducts.toList()
    }
}