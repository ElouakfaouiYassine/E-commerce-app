package com.example.myapplication.Repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseUser(context:Context):
    SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION){

    companion object{
        const val DATABASE_NAME = "UserDatabase.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "data"
        const val COLUMN_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_PROFILE_IMAGE_URI = "profile_image_uri"
        const val COLUMN_Is_Admin = "is_admin"

    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME ("+ "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $COLUMN_USERNAME TEXT, "+
                " $COLUMN_EMAIL TEXT, "+
                " $COLUMN_PHONE TEXT, "+
                " $COLUMN_PASSWORD TEXT, "+
                " $COLUMN_PROFILE_IMAGE_URI TEXT, "+
                "$COLUMN_Is_Admin INTEGER DEFAULT 0)")
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
       /* val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)*/
    }
    fun insertUser(username: String, email: String, phone: String, password: String, isAdmin: Boolean):Long{
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PHONE, phone)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_Is_Admin, if (isAdmin) 1 else 0)
        }
        val db = writableDatabase
        return db.insert(TABLE_NAME, null, values)
    }
    fun updateUserByEmail(oldEmail:String, newUserName:String, newEmail:String, newPhoneNumber:String, newPassword:String):Int{
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, newUserName)
            put(COLUMN_EMAIL, newEmail)
            put(COLUMN_PHONE, newPhoneNumber)
            put(COLUMN_PASSWORD, newPassword)
        }
        return try {
            val whereClause = "$COLUMN_EMAIL = ?"
            val whereArgs = arrayOf(oldEmail)
            db.update(TABLE_NAME, values, whereClause, whereArgs)
        }catch (e: Exception){
            e.printStackTrace()
            -1
        }
    }
    fun deleteUserByName(email: String): Int {
        val db = writableDatabase
        return try {
            val whereClause = "$COLUMN_EMAIL = ?"
            val whereArgs = arrayOf(email)
            db.delete(TABLE_NAME, whereClause, whereArgs)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    fun updateUserProfileImage(email: String, imageUri: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PROFILE_IMAGE_URI, imageUri)
        }
        return try {
            val whereClause = "$COLUMN_EMAIL = ?"
            val whereArgs = arrayOf(email)
            db.update(TABLE_NAME, values, whereClause, whereArgs)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    fun getInfoUtilusateur():Cursor?{
        val p0 = this.writableDatabase
        val cursor = p0.rawQuery("select * from $TABLE_NAME", null)
        return cursor
    }
    fun isUserAdmin(username: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_Is_Admin = ?"
        val selectionArgs = arrayOf(username, "1")
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)
        val isAdmin = cursor.count > 0
        cursor.close()
        return isAdmin
    }


    fun readUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

}