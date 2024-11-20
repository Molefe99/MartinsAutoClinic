package com.example.martinsautoclinic


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "QuotesDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        // Create the Quotes table without inserting any initial data
        db?.execSQL(
            """
            CREATE TABLE Quotes (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                customerName TEXT NOT NULL, 
                requestedService TEXT NOT NULL, 
                estimatedCost TEXT NOT NULL
            )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop the old table if it exists and create a new one
        db?.execSQL("DROP TABLE IF EXISTS Quotes")
        onCreate(db)
    }

    /**
     * Fetch all quotes from the database.
     */
    fun fetchQuotes(): List<Quote> {
        val quotes = mutableListOf<Quote>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Quotes", null)

        if (cursor.moveToFirst()) {
            do {
                val customerName = cursor.getString(cursor.getColumnIndexOrThrow("customerName"))
                val requestedService = cursor.getString(cursor.getColumnIndexOrThrow("requestedService"))
                val estimatedCost = cursor.getString(cursor.getColumnIndexOrThrow("estimatedCost"))
                quotes.add(Quote(customerName, requestedService, estimatedCost))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return quotes
    }
}
