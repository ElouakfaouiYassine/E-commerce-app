package com.example.myapplication.Model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Products(
    var id_product: Int,
    var image_product: String,
    var nam_Product: String,
    var description_Product: String,
    var quantity_Product: Int,
    var price_Product: Double,
    var discount_Price_Product: Double,
    var quantity_order: Int = 0,
    var isInCart: Boolean = false,
    var isLiked: Boolean = false,
    var isSelected: Boolean = false
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    constructor() : this(0, "", "", "", 0,  0.0,0.0,0, false, false)
    constructor(
        id_product: Int,
        image_product: String,
        nam_Product: String,
        description_Product: String,
        quantity_Product: Int,
        price_Product: Double,
        discount_Price_Product: Double
    ) : this(id_product, image_product, nam_Product, description_Product, quantity_Product, price_Product, discount_Price_Product,0,  false, false)



    constructor(image_product: String, nam_Product: String, description_Product: String, quantity_Product: Int, price_Product: Double, discount_Price_Product: Double) :
            this(0, image_product, nam_Product,description_Product,quantity_Product, price_Product,discount_Price_Product)


    constructor(id_product: Int, image_product: String, nam_Product: String, description_Product: String, quantity_Product: Int, price_Product: Double, discount_Price_Product: Double, isLiked: Boolean) :
            this(id_product, image_product, nam_Product,description_Product,quantity_Product, price_Product, discount_Price_Product,0, false, isLiked)


    constructor(image_product: String, nam_Product: String, description_Product: String, price_Product: Double,discount_Price_Product: Double, isInCart: Boolean, isLiked: Boolean) :
            this(0, image_product, nam_Product,description_Product,0, price_Product, discount_Price_Product,0, isInCart, isLiked)

    constructor(image_product: String, nam_Product: String, description_Product: String, quantity_Product: Int, price_Product: Double, discount_Price_Product: Double, isInCart: Boolean, isLiked: Boolean) :
            this(0, image_product, nam_Product,description_Product,quantity_Product, price_Product, discount_Price_Product, 0, isInCart, isLiked)



    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id_product)
        parcel.writeString(image_product)
        parcel.writeString(nam_Product)
        parcel.writeString(description_Product)
        parcel.writeInt(quantity_Product)
        parcel.writeDouble(price_Product)
        parcel.writeDouble(discount_Price_Product)
        parcel.writeByte(if (isInCart) 1 else 0)
        parcel.writeByte(if (isLiked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Products> {
        override fun createFromParcel(parcel: Parcel): Products {
            return Products(parcel)
        }

        override fun newArray(size: Int): Array<Products?> {
            return arrayOfNulls(size)
        }
    }
}