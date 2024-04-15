package com.example.myapplication.Repository

import android.content.Context
import com.example.myapplication.Model.Products

object ProductsData {
    /*fun getProdactData(context: Context): ArrayList<Products> {
        val listProductsData = ArrayList<Products>()
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            DatabaseHelper.COLUMN_IMAGE,
            DatabaseHelper.COLUMN_NAME,
            DatabaseHelper.COLUMN_PRICE,
            DatabaseHelper.COLUMN_ID
        )

        val cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                listProductsData.add(Products(this))
            }
        }

        cursor.close()
        db.close()

        return listProductsData
    }*/
}