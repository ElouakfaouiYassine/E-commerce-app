package com.example.myapplication.Repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBasePayment(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        const val DATABASE_NAME = "Payment_Database.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "payment_data"
        const val COLUMN_ID = "id"
        const val COLUMN_CARD_NUMBER = "card_number"
        const val COLUMN_CARD_NAME = "card_name"
        const val COLUMN_EXPIRATION = "expiration"
        const val COLUMN_CVV = "cvv"

    }
    override fun onCreate(db: SQLiteDatabase?) {
        val creatTableQuery = ("CREATE TABLE $TABLE_NAME ("+
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_CARD_NUMBER TEXT, " +
                "$COLUMN_CARD_NAME TEXT, " +
                "$COLUMN_EXPIRATION TEXT, " +
                "$COLUMN_CVV TEXT )")
        db?.execSQL(creatTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertInfoCard(card_number:String, card_name:String, card_expiration:String, card_cvv:String):Long{
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_CARD_NUMBER = ?", arrayOf(card_number))
        if (cursor != null && cursor.moveToFirst()){
            cursor.close()
            return -1L
        }

        val values = ContentValues().apply {
            put(COLUMN_CARD_NUMBER, card_number)
            put(COLUMN_CARD_NAME, card_name)
            put(COLUMN_EXPIRATION, card_expiration)
            put(COLUMN_CVV, card_cvv)
        }

        return try {
            db.insert(TABLE_NAME, null, values)
        }catch (e:Exception){
            e.printStackTrace()
            -1L
        }
    }

}