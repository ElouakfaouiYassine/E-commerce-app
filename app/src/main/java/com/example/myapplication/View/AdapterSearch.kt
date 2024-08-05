package com.example.myapplication.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class AdapterSearch(var list:List<Products>, private val onItemClick: SearchFragment): RecyclerView.Adapter<AdapterSearch.CategoryViewHolder>(),
    Filterable {
    var currentCategory: String = ""
    private var filteredList: List<Products> = list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.info_rv_search, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount()= filteredList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentCategory = filteredList[position]
        holder.bind(currentCategory)
    }
    fun updateDataCategory(newList: List<Products>) {
        list = newList
        filteredList = newList
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(product: Products)
        fun onProductImageClicked(product: Products)
        fun onAddProductClicked(product: Products)
    }
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageview_info_Category: ImageView = itemView.findViewById(R.id.image_info_category)
        var image_add_Panier: ImageView = itemView.findViewById(R.id.icon_add_panier)
        var tvname_info_Category : TextView = itemView.findViewById(R.id.info_tv_namP_category)
        var tvprice_info_Category : TextView = itemView.findViewById(R.id.info_tv_priceP_category)
        var tvprice_description_info_Category: TextView = itemView.findViewById(R.id.info_tv_description_search)
        var tvdiscount_price_info_Search: TextView = itemView.findViewById(R.id.info_tv_discount_search)
        init {
            image_add_Panier.setOnClickListener {
                onItemClick.onAddProductClicked(list[adapterPosition])
            }

        }
        fun bind(product: Products) {
            tvname_info_Category.text = product.name
            tvprice_info_Category.text = product.price.toString()
            tvprice_description_info_Category.text = product.description.toString()
            tvdiscount_price_info_Search.text = product.price_promotion.toString()

            Glide.with(itemView.context)
                .load(product.image) // Pass your image URI or URL here
                .placeholder(R.drawable.background_shape) // Placeholder image while loading
                .error(R.drawable.background_error) // Error image if loading fails
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Caching strategy
                .into(imageview_info_Category)

            val currencyFormat = NumberFormat.getCurrencyInstance()
            currencyFormat.currency = Currency.getInstance("MAD")

            val formattedPrice = currencyFormat.format(product.price)
            tvprice_info_Category.text = formattedPrice
            val formattedPriceDiscount = currencyFormat.format(product.price_promotion)
            tvdiscount_price_info_Search.text = formattedPriceDiscount

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick.onItemClick(filteredList[position])
                }
            }
            imageview_info_Category.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick.onProductImageClicked(filteredList[position])
                }
            }
            if (product.isInCart) {
                image_add_Panier.visibility = View.INVISIBLE
            } else {
                image_add_Panier.visibility = View.VISIBLE
            }
            image_add_Panier.setOnClickListener {
                onItemClick.onAddProductClicked(product)
            }

            /*product.image_product?.let { imageUri ->
                imageview_info_Category.setImageURI(imageUri)
            }*/
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults = mutableListOf<Products>()
                if (constraint.isNullOrBlank()) {
                    filteredResults.addAll(list)
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    for (item in list) {
                        if (item.name.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                            filteredResults.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredResults
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<Products>
                notifyDataSetChanged()
            }
        }
    }

}