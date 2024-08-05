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
        const val COLUMN_QUANTITY_ORDER = "quantity_order"
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
                "$COLUMN_QUANTITY_ORDER INTEGER, " +
                "$COLUMN_PROMOTION_PRICE TEXT, " +
                "$COLUMN_PRICE TEXT)")
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // This adds the new column to the existing table without losing data
            db?.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_QUANTITY_ORDER INTEGER DEFAULT 0")
        }
    }

    fun getExistingQuantity(db: SQLiteDatabase, productName: String): Int {
        val cursor = db.query(
            TABLE_NAME, arrayOf(COLUMN_QUANTITY_ORDER),  // Ensure this column is correctly named and included in the query
            "$COLUMN_NAME = ?",
            arrayOf(productName),
            null, null, null
        )
        cursor.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(COLUMN_QUANTITY_ORDER)
                if (columnIndex != -1) {
                    return it.getInt(columnIndex)
                }
            }
        }
        return 0  // Return 0 if product is not found or column is missing
    }


    fun addToCart(product: Products, quantity: Int): Long {
        val db = writableDatabase
        val existingQuantity = getExistingQuantity(db, product.name)

        if (product.quantity < quantity + existingQuantity) {
            return -1L // Not enough stock
        }

        val values = ContentValues().apply {
            put(COLUMN_IMAGE, product.image?.toString())
            put(COLUMN_NAME, product.name)
            put(COLUMN_DESCRIPTION, product.description)
            put(COLUMN_QUANTITY_ORDER, quantity + existingQuantity) // Assuming you are tracking ordered quantity
            put(COLUMN_PRICE, product.price)
            put(COLUMN_PROMOTION_PRICE, product.price_promotion)
        }

        // Update if already in cart, otherwise insert new
        return if (existingQuantity > 0) {
            db.update(TABLE_NAME, values, "$COLUMN_NAME = ?", arrayOf(product.name)).toLong()
        } else {
            db.insert(TABLE_NAME, null, values)
        }
    }






    /*fun addToCart(product: Products): Long {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_IMAGE, product.image_product?.toString())
            put(COLUMN_NAME, product.nam_Product)
            put(COLUMN_DESCRIPTION, product.description_Product)
            put(COLUMN_QUANTITY, product.quantity_Product)
            put(COLUMN_QUANTITY_ORDER, product.quantity_order)
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
    }*/
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
