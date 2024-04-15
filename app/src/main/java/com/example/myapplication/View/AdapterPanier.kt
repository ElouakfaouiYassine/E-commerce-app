package com.example.myapplication.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import java.text.NumberFormat

class AdapterPanier(var list:List<Products>, private val deleteListener: (Products) -> Unit, var itemClick: OnItemClickListener) : RecyclerView.Adapter<AdapterPanier.PanierViewHolder>() {
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
    }
    inner class PanierViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        }
        fun bind(product: Products) {
            Glide.with(itemView.context).load(product.image_product).into(imageviewInfo)
            tvnameInfo.text = product.nam_Product
            tvdescriptionInfo.text = product.description_Product
            tvquantityInfo.text = product.quantity_Product.toString()
            tvpriceInfo.text = product.price_Product.toString()
            tvpriceDiscountInfo.text = product.discount_Price_Product.toString()
            val formattedPrice = NumberFormat.getCurrencyInstance().format(product.price_Product)
            tvpriceInfo.text = formattedPrice
            val formattedPriceDiscount = NumberFormat.getCurrencyInstance().format(product.discount_Price_Product)
            tvpriceDiscountInfo.text = formattedPriceDiscount

            imageDeletePanier.setOnClickListener {
                deleteListener(product)
            }
        }
    }
}