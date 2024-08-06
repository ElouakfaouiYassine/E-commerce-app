package com.example.myapplication.View


import android.util.Log
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
import java.util.Currency

class MyAdapter(
    private var list: List<Products>,
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
        private val relative_click:RelativeLayout = itemView.findViewById(R.id.click_product)
        private val image_add_panier: ImageView = itemView.findViewById(R.id.icon_add_Panier)
        private val imageview_info: ImageView = itemView.findViewById(R.id.image_info)
        private val tvdiscount_price_info: TextView = itemView.findViewById(R.id.info_tv_discount_price)
        private val tvdescription_info: TextView = itemView.findViewById(R.id.info_tv_description)
        private val tvprice_info: TextView = itemView.findViewById(R.id.info_tv_priceP)
        private val tvname_info: TextView = itemView.findViewById(R.id.info_tv_namP)

        init {
            itemView.setOnClickListener {
                onItemClick.onItemClick(list[adapterPosition])
            }

            relative_click.setOnClickListener {
                onItemClick.onProductImageClicked(list[adapterPosition])
            }

            image_add_panier.setOnClickListener {
                onItemClick.onAddProductClicked(list[adapterPosition])
            }
        }

        fun bind(product: Products) {
            with(itemView) {

                val baseUrl = "http://192.168.43.164/e-commerce%20app%20mobile%20back/"
                val imageUrl = baseUrl + product.image

                Log.d("ImageURL", imageUrl)

                Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.background_error)
                    .error(R.drawable.background_error)
                    .into(imageview_info)

                tvdiscount_price_info.text = product.price_promotion.toString()
                tvdescription_info.text = product.description
                tvprice_info.text = product.price.toString()
                tvname_info.text = product.name

                val currencyFormat = NumberFormat.getCurrencyInstance()
                currencyFormat.currency = Currency.getInstance("MAD")

                // Format prices
                val formattedPrice = currencyFormat.format(product.price)
                tvprice_info.text = formattedPrice

                val formattedPriceDiscount = currencyFormat.format(product.price_promotion)
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
