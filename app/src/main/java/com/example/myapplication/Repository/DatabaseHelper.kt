package com.example.myapplication.Repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "Products_Database.db"
        const val DATABASE_VERSION = 2
        const val TABLE_NAME = "products_data"
        const val COLUMN_ID = "id"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_QUANTITY = "quantity"
        const val COLUMN_PRICE = "price"
        const val COLUMN_PROMOTION_PRICE = "promotion_price"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_IS_LIKED = "is_liked"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME ("+
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_IMAGE TEXT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_DESCRIPTION TEXT, " +
                "$COLUMN_QUANTITY TEXT, " +
                "$COLUMN_PRICE TEXT, " +
                "$COLUMN_PROMOTION_PRICE TEXT, " +
                "$COLUMN_CATEGORY TEXT, " +
                "$COLUMN_IS_LIKED INTEGER DEFAULT 0)")
        db?.execSQL(createTableQuery)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    @SuppressLint("Range")
    fun isProductLiked(productName: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME, arrayOf(COLUMN_IS_LIKED),
            "$COLUMN_NAME = ?", arrayOf(productName),
            null, null, null
        )
        var isLiked = false
        cursor?.use {
            if (it.moveToFirst()) {
                val isLikedInt = it.getInt(it.getColumnIndex(COLUMN_IS_LIKED))
                isLiked = isLikedInt == 1
            }
        }
        return isLiked
    }

    fun updateProductLikeStatus(productName: String, isLiked: Boolean): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IS_LIKED, if (isLiked) 1 else 0) // Store 1 for liked, 0 for not liked
        }
        return try {
            val whereClause = "$COLUMN_NAME = ?"
            val whereArgs = arrayOf(productName)
            db.update(TABLE_NAME, values, whereClause, whereArgs)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }
    fun insertProducts(image: String, name: String, description: String, quantity: String, price: String, promotion_price: String, category: String ): Long {
        val db = writableDatabase

        // Check if the product with the same name already exists
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME = ?", arrayOf(name))
        if (cursor != null && cursor.moveToFirst()) {
            // Product with the same name already exists, return -1L or handle accordingly
            cursor.close()
            return -1L
        }

        val values = ContentValues().apply {
            put(COLUMN_IMAGE, image)
            put(COLUMN_NAME, name)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_QUANTITY, quantity)
            put(COLUMN_PRICE, price)
            put(COLUMN_PROMOTION_PRICE, promotion_price)
            put(COLUMN_CATEGORY, category)
        }

        return try {
            // Product does not exist, insert it into the database
            db.insert(TABLE_NAME, null, values)
        } catch (e: Exception) {
            e.printStackTrace()
            -1L
        }
    }


    fun getInfoProducts(): Cursor?{
        val p0 = this.writableDatabase
        val cursor = p0.rawQuery("select * from $TABLE_NAME", null)
        return cursor
    }

    fun searchProducts(query: String): Cursor? {
        val db = readableDatabase
        val selection = "$COLUMN_NAME LIKE ?"
        val selectionArgs = arrayOf("%$query%")
        return db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)
    }


    fun updateProductByName(oldName: String, newImage: Uri?, newName: String, newDescription: String, newQuantity: String, newPrice: String, newPromotion: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IMAGE, newImage?.toString()) // Convert Uri to String
            put(COLUMN_NAME, newName)
            put(COLUMN_DESCRIPTION, newDescription)
            put(COLUMN_QUANTITY, newQuantity)
            put(COLUMN_PRICE, newPrice)
            put(COLUMN_PROMOTION_PRICE, newPromotion)
        }
        return try {
            val whereClause = "$COLUMN_NAME = ?"
            val whereArgs = arrayOf(oldName)
            db.update(TABLE_NAME, values, whereClause, whereArgs)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    fun getProductsByCategory(category: String): Cursor? {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_CATEGORY = ?", arrayOf(category))
    }
    fun deleteProductByName(name: String): Int {
        val db = writableDatabase
        return try {
            val whereClause = "$COLUMN_NAME = ?"
            val whereArgs = arrayOf(name)
            db.delete(TABLE_NAME, whereClause, whereArgs)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }
}
