package com.example.myapplication.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Model.OrderItem
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import java.text.NumberFormat
import java.util.Currency

class AdapterOrder(private var productList: List<OrderItem>, private val listener: OnItemClickListener) : RecyclerView.Adapter<AdapterOrder.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.info_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val currentOrder = productList[position]
        holder.bind(currentOrder)

    }

    fun updateData(newList: List<OrderItem>) {
        productList = newList
        notifyDataSetChanged()
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageOrder: ImageView = itemView.findViewById(R.id.image_order)
        private val nameOrder: TextView = itemView.findViewById(R.id.info_tv_nam_order)
        private val descriptionOrder: TextView = itemView.findViewById(R.id.info_tv_description_order)
        private val priceOrder: TextView = itemView.findViewById(R.id.info_tv_price_order)
        private val pricePromotionOrder: TextView = itemView.findViewById(R.id.info_tv_pricePromorion_order)
        private val returnOrder: Button = itemView.findViewById(R.id.btn_return_order)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(productList[position])
                }
            }

        }

        fun bind(orderItem: OrderItem) {
            nameOrder.text = orderItem.name
            descriptionOrder.text = orderItem.description
            priceOrder.text = orderItem.price.toString()
            pricePromotionOrder.text = orderItem.price_promotion.toString()
            Glide.with(itemView)
                .load(orderItem.image)
                .into(imageOrder)

            val currencyFormat = NumberFormat.getCurrencyInstance()
            currencyFormat.currency = Currency.getInstance("MAD")

            // Format prices
            val formattedPrice = currencyFormat.format(orderItem.price)
            priceOrder.text = formattedPrice

            val formattedPriceDiscount = currencyFormat.format(orderItem.price_promotion)
            pricePromotionOrder.text = formattedPriceDiscount
        }
    }

    interface OnItemClickListener {
        fun onItemClick(product: OrderItem)
    }
}
