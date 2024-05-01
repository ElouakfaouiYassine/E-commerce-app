package com.example.myapplication.View

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import java.text.NumberFormat
import java.util.Currency

class MyAdapter(private var list: List<Products>,
                private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.info_rv, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun updateData(newList: List<Products>) {
        list = newList
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(product: Products)
        fun onAddProductClicked(product: Products)
        fun onDeleteProductClicked(product: Products)
        fun onProductImageClicked(product: Products)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image_add_panier: ImageView = itemView.findViewById(R.id.icon_add_Panier)
        private val imageview_info: ImageView = itemView.findViewById(R.id.image_info)
        private val tvdiscount_price_info: TextView =
            itemView.findViewById(R.id.info_tv_discount_price)
        private val tvdescription_info: TextView = itemView.findViewById(R.id.info_tv_description)
        private val tvprice_info: TextView = itemView.findViewById(R.id.info_tv_priceP)
        private val tvname_info: TextView = itemView.findViewById(R.id.info_tv_namP)

        init {
            itemView.setOnClickListener {
                onItemClick.onItemClick(list[adapterPosition])
            }

            imageview_info.setOnClickListener {
                onItemClick.onProductImageClicked(list[adapterPosition])
            }

            image_add_panier.setOnClickListener {
                onItemClick.onAddProductClicked(list[adapterPosition])
            }
        }

        fun bind(product: Products) {
            with(itemView) {
                Glide.with(context).load(product.image_product).into(imageview_info)
                tvdiscount_price_info.text = product.discount_Price_Product.toString()
                tvdescription_info.text = product.description_Product
                tvprice_info.text = product.price_Product.toString()
                tvname_info.text = product.nam_Product

                val currencyFormat = NumberFormat.getCurrencyInstance()
                currencyFormat.currency = Currency.getInstance("MAD")

                // Format prices
                val formattedPrice = currencyFormat.format(product.price_Product)
                tvprice_info.text = formattedPrice

                val formattedPriceDiscount = currencyFormat.format(product.discount_Price_Product)
                tvdiscount_price_info.text = formattedPriceDiscount



                if (product.isInCart) {
                    image_add_panier.visibility = View.INVISIBLE
                } else {
                    image_add_panier.visibility = View.VISIBLE
                }

                image_add_panier.setOnClickListener {
                    onItemClick.onAddProductClicked(product)

                }
            }
        }
    }
}