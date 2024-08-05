package com.example.myapplication.Model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Products(
    var id: Int,
    var image: String,
    var name: String,
    var description: String,
    var quantity: Int,
    var price: Double,
    var price_promotion: Double,
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
        id: Int,
        image: String,
        name: String,
        description: String,
        quantity: Int,
        price: Double,
        price_promotion: Double
    ) : this(id, image, name, description, quantity, price, price_promotion,0,  false, false)

    constructor(image: String, name: String, description: String, quantity: Int, price: Double, price_promotion: Double) :
            this(0, image, name,description,quantity, price,price_promotion)


    constructor(id: Int, image: String, name: String, description: String, quantity: Int, price: Double, price_promotion: Double, isLiked: Boolean) :
            this(id, image, name,description,quantity, price, price_promotion,0, false, isLiked)


    constructor(image: String, name: String, description: String, price: Double,price_promotion: Double, isInCart: Boolean, isLiked: Boolean) :
            this(0, image, name,description,0, price, price_promotion,0, isInCart, isLiked)

    constructor(image: String, name: String, description: String, quantity: Int, price: Double, price_promotion: Double, isInCart: Boolean, isLiked: Boolean) :
            this(0, image, name,description,quantity, price, price_promotion, 0, isInCart, isLiked)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(image)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(quantity)
        parcel.writeDouble(price)
        parcel.writeDouble(price_promotion)
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
