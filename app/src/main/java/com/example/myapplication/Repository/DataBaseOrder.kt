package com.example.myapplication.Repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import com.example.myapplication.Model.OrderItem
import com.example.myapplication.Model.Products

class DataBaseOrder(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "Order_Database.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME_ORDER = "order_product"
        const val COLUMN_ID_ORDER = "id"
        const val COLUMN_IMAGE_ORDER = "image_order"
        const val COLUMN_NAME_PRODUCT_ORDER = "name_product"
        const val COLUMN_DESCRIPTION_ORDER = "description_product"
        const val COLUMN_PRICE_ORDER = "price_product"
        const val COLUMN_PRICE_PROMOTION_ORDER = "price_promotion_product"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME_ORDER (" +
                "$COLUMN_ID_ORDER INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_IMAGE_ORDER TEXT, " +
                "$COLUMN_NAME_PRODUCT_ORDER TEXT, " +
                "$COLUMN_DESCRIPTION_ORDER TEXT, " +
                "$COLUMN_PRICE_ORDER TEXT, " +
                "$COLUMN_PRICE_PROMOTION_ORDER TEXT)")
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Implement if needed
    }

    fun insertOrder(product: OrderItem) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IMAGE_ORDER, product.image.toString())
            put(COLUMN_NAME_PRODUCT_ORDER, product.name)
            put(COLUMN_DESCRIPTION_ORDER, product.description)
            put(COLUMN_PRICE_ORDER, product.price.toString()) // Assuming price is stored as text
            put(COLUMN_PRICE_PROMOTION_ORDER, product.price_promotion.toString())
        }
        db.insert(TABLE_NAME_ORDER, null, values)
        db.close()
    }

    fun getAllOrders(): List<OrderItem> {
        val orders = mutableListOf<OrderItem>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME_ORDER, null, null, null, null, null, null)

        cursor?.use {

            val idIndex = cursor.getColumnIndex(COLUMN_ID_ORDER)
            val imageIndex = cursor.getColumnIndex(COLUMN_IMAGE_ORDER)
            val nameIndex = cursor.getColumnIndex(COLUMN_NAME_PRODUCT_ORDER)
            val descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION_ORDER)
            val priceIndex = cursor.getColumnIndex(COLUMN_PRICE_ORDER)
            val pricepromotionIndex = cursor.getColumnIndex(COLUMN_PRICE_PROMOTION_ORDER)

            while (cursor.moveToNext()) {

                val id = cursor.getLong(idIndex)
                val image = cursor.getString(imageIndex)
                val name = cursor.getString(nameIndex)
                val description = cursor.getString(descriptionIndex)
                val price = cursor.getDouble(priceIndex)
                val price_promotion = cursor.getDouble(pricepromotionIndex)
                val orderItem = OrderItem(id, image, name, description, price, price_promotion)
                orders.add(orderItem)
            }
        }
        return orders
    }
}
