package com.example.myapplication.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import java.text.NumberFormat

class AdapterFavorite(var list: List<Products>,  var itemClick: OnItemClickListener): RecyclerView.Adapter<AdapterFavorite.ViewHolderFavorite>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFavorite {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.info_rv_favorite, parent, false)
        return ViewHolderFavorite(view)
    }

    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: ViewHolderFavorite, position: Int) {
        val currentFavorite = list[position]
        holder.image_favorite.setImageURI(currentFavorite.image_product)
        holder.tvname_favorite.text = currentFavorite.nam_Product
        holder.tvdescription_favorite.text = currentFavorite.description_Product
        holder.tvquantity_favorite.text = currentFavorite.quantity_Product.toString()
        holder.tvprice_favorite.text = currentFavorite.price_Product.toString()
        holder.tvprice_Promotion_favorite.text = currentFavorite.discount_Price_Product .toString()


        val formattedPrice = NumberFormat.getCurrencyInstance().format(currentFavorite.price_Product)
        holder.tvprice_favorite.text = formattedPrice
        val formattedPriceDiscount = NumberFormat.getCurrencyInstance().format(currentFavorite.discount_Price_Product)
        holder.tvprice_Promotion_favorite .text = formattedPriceDiscount
    }

    interface OnItemClickListener {
        fun onItemClick(product: Products)
        fun onProductImageClicked(product: Products)
    }

    inner class ViewHolderFavorite(itemView: View): RecyclerView.ViewHolder(itemView){
        var image_favorite: ImageView = itemView.findViewById(R.id.image_Favorite)
        var tvname_favorite: TextView = itemView.findViewById(R.id.info_tv_namP_Favorite)
        var tvdescription_favorite: TextView = itemView.findViewById(R.id.info_tv_description_Favorite)
        var tvquantity_favorite: TextView = itemView.findViewById(R.id.info_tv_quantity_Favorite)
        var tvprice_favorite: TextView = itemView.findViewById(R.id.info_tv_priceP_Favorite)
        var tvprice_Promotion_favorite: TextView = itemView.findViewById(R.id.info_tv_pricePromotion_Favorite)

        init {

            itemView.setOnClickListener {
                itemClick.onItemClick(list[adapterPosition])
            }

            image_favorite.setOnClickListener {
                itemClick.onProductImageClicked(list[adapterPosition])
            }

        }

    }
}