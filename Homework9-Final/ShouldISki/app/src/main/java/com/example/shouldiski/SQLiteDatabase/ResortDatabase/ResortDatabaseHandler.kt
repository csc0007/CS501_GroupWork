package com.example.shouldiski.SQLiteDatabase.ResortDatabase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast



const val DATABASE_NAME = "ResortDatabase"
const val TABLE_NAME = "ResortDataTable"
const val COL_DESTINATION = "Destination"
const val COL_RESORTNAME = "ResortName"
const val COL_HOTELID = "HotelID"
const val COL_RESORTLOCATION = "ResortLocation"
const val COL_ID = "ID"

class ResortDatabaseHandler(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DESTINATION + " VARCHAR(256), " +
                COL_RESORTNAME + " VARCHAR(256), " +
                COL_HOTELID + " VARCHAR(256), " +
                COL_RESORTLOCATION + " VARCHAR(256))"

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun importCSV(fileName: String) {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")   //clear up existing data before import new
        context.assets.open(fileName).bufferedReader().useLines { lines ->
            lines.drop(1).forEach { line -> // Skip the header
                val tokens = line.split(',')
                if (tokens.size >= 4) { // Ensure there are enough columns
                    val resort = Resort(tokens[0], tokens[1], tokens[2], tokens[3])
                    insertData(resort)
                }
            }
        }
    }

    fun insertData(resort : Resort){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_DESTINATION,resort.destination)
        cv.put(COL_RESORTNAME,resort.resortName)
        cv.put(COL_HOTELID,resort.hotelID)
        cv.put(COL_RESORTLOCATION,resort.resortLocation)
        var result = db.insert(TABLE_NAME,null,cv)
        if(result == -1.toLong())
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("Range")
    fun getHotelId(destination: String): String? {
        val db = this.readableDatabase
        val selectQuery = "SELECT $COL_HOTELID FROM $TABLE_NAME WHERE $COL_DESTINATION = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(destination))
        var hotelId: String? = null
        if (cursor.moveToFirst()) {
            hotelId = cursor.getString(cursor.getColumnIndex(COL_HOTELID))
        }
        cursor.close()
        return hotelId
    }

    @SuppressLint("Range")
    fun getResortName(destination: String): String? {
        val db = this.readableDatabase
        val selectQuery = "SELECT $COL_RESORTNAME FROM $TABLE_NAME WHERE $COL_DESTINATION = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(destination))
        var resortName: String? = null
        if (cursor.moveToFirst()) {
            resortName = cursor.getString(cursor.getColumnIndex(COL_RESORTNAME))
        }
        cursor.close()
        return resortName
    }

    // Query through the database to check if input destination is in database.
    // If in database, return 1, otherwise return 0
    fun checkDestination(destination: String): Boolean {
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_DESTINATION = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(destination))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}