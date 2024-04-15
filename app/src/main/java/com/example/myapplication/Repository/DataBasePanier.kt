package com.example.myapplication.Repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.Model.Products

class DataBasePanier(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "Panier_Database.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "panier_data"
        const val COLUMN_ID = "id"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_QUANTITY = "quantity"
        const val COLUMN_PRICE = "price"
        const val COLUMN_PROMOTION_PRICE = "promotion_price"
        // Add other columns as needed
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME ("+
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_IMAGE TEXT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_DESCRIPTION TEXT, " +
                "$COLUMN_QUANTITY TEXT, " +
                "$COLUMN_PROMOTION_PRICE TEXT, " +
                "$COLUMN_PRICE TEXT)")
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades if needed
    }

    fun addToCart(product: Products): Long {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_IMAGE, product.image_product?.toString())
            put(COLUMN_NAME, product.nam_Product)
            put(COLUMN_DESCRIPTION, product.description_Product)
            put(COLUMN_QUANTITY, product.quantity_Product)
            put(COLUMN_PRICE, product.price_Product)
            put(COLUMN_PROMOTION_PRICE, product.discount_Price_Product)
        }

        return try {
            // Insert the product into the cart table
            db.insert(TABLE_NAME, null, values)
        } catch (e: Exception) {
            e.printStackTrace()
            -1L
        }
    }
    fun isProductInCart(productName: String): Boolean {
        val db = readableDatabase
        val query = "SELECT COUNT(*) FROM $TABLE_NAME WHERE $COLUMN_NAME = ?"
        val cursor = db.rawQuery(query, arrayOf(productName))
        cursor.use {
            if (it.moveToFirst()) {
                val count = it.getInt(0)
                return count > 0
            }
        }
        return false
    }
    fun removeFromCart(productName: String): Int {
        val db = writableDatabase
        return try {
            // Delete the product from the cart table
            db.delete(TABLE_NAME, "$COLUMN_NAME = ?", arrayOf(productName))
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }
    fun getAllProductsInCart(): Cursor? {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }
}
