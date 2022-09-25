package com.bhavitbj.newsbreeze.database

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.bhavitbj.newsbreeze.utils.Konstant
import com.bhavitbj.newsbreeze.model.Article
import java.util.ArrayList

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, TABLE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val drop = "DROP IF TABLE EXISTS "
        db.execSQL(drop + TABLE_NAME)
        onCreate(db)
    }

    fun addData(
        title: String, image_url: String?, description: String?,
        author: String?, content: String?, url: String?,
        date: String?
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL2, Konstant.escapingString(title))
        contentValues.put(COL3, image_url)
        contentValues.put(COL4, description)
        contentValues.put(COL5, author)
        contentValues.put(COL6, content)
        contentValues.put(COL7, url)
        contentValues.put(COL8, date)
        contentValues.put(COL9, "1")
        Log.e(TAG, "addDATA: Adding " + title + " to " + TABLE_NAME)
        val result = db.insert(TABLE_NAME, null, contentValues)
        return if (result == -1L) {
            false
        } else {
            true
        }
    }


    val allData: Cursor
        get() {
            val db = this.writableDatabase
            val query = "SELECT * FROM " + TABLE_NAME
            return db.rawQuery(query, null)
        }

    fun getSpecificItemsID(date: String): Boolean {
        Log.e("DbHelper", "date: $date")
        val db = this.writableDatabase
        val query = "SELECT " + COL8 + " FROM " + TABLE_NAME +
                " WHERE " + COL8 + " = '" + date + "'"
        val data = db.rawQuery(query, null)
        while (data.moveToNext()) {
            val cDate = data.getString(data.getColumnIndexOrThrow(COL8))
            if (cDate == date) {
                Log.e("DbHelper", "Data matched: true")
                return true
            }
        }
        return false
    }


    fun deleteDataFromDB(date: String) {
        val db = this.writableDatabase
        val query = ("DELETE FROM " + TABLE_NAME + " WHERE "
                + COL8 + " = '" + date + "'")
        Log.e(TAG, "deleteMessage: query: $query")
        db.execSQL(query)
    }


    val articleData:


            ArrayList<Article>
        get() {
            val arrayList = ArrayList<Article>()


            val select_query = "SELECT * FROM " + TABLE_NAME
            val db = this.writableDatabase
            val cursor = db.rawQuery(select_query, null)


            if (cursor.moveToFirst()) {
                do {
                    val articleModel = Article()
                    articleModel.title = cursor.getString(cursor.getColumnIndexOrThrow(COL2))
                    articleModel.urlToImage = cursor.getString(cursor.getColumnIndexOrThrow(COL3))
                    articleModel.description = cursor.getString(cursor.getColumnIndexOrThrow(COL4))
                    articleModel.author = cursor.getString(cursor.getColumnIndexOrThrow(COL5))
                    articleModel.content = cursor.getString(cursor.getColumnIndexOrThrow(COL6))
                    articleModel.url = cursor.getString(cursor.getColumnIndexOrThrow(COL7))
                    articleModel.publishedAt = cursor.getString(cursor.getColumnIndexOrThrow(COL8))
                    articleModel.bookmarkStatus = cursor.getString(cursor.getColumnIndexOrThrow(COL9))
                    arrayList.add(articleModel)
                } while (cursor.moveToNext())
            }
            return arrayList
        }

    companion object {
        private const val TAG = "DatabaseHelper"
        private const val TABLE_NAME = "my_news_db"
        const val COL1 = "ID"
        const val COL2 = "title"
        const val COL3 = "image_url"
        const val COL4 = "description"
        const val COL5 = "author"
        const val COL6 = "content"
        const val COL7 = "url"
        const val COL8 = "date"
        const val COL9 = "bookmarkStatus"
        private const val CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL2 + " TEXT," + COL3 + " TEXT," + COL4 + " TEXT," + COL5 + " TEXT," + COL6 + " TEXT," +
                    COL7 + " TEXT," + COL8 + " TEXT," + COL9 + " TEXT)"
    }
}