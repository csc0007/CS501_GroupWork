package com.example.shouldiski.SQLiteDatabase.PreferenceDatabase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast



const val DATABASE_NAME = "PreferenceDatabase"
const val TABLE_NAME = "PreferenceDataTable"
const val COL_POWDER = "PowderSnow"
const val COL_GROOMED = "GroomedSnow"
const val COL_COLDWEATHER = "ColdWeather"
const val COL_WARMWEATHER = "WarmWeather"
const val COL_SNOWWEATHER = "SnowWeather"
const val COL_CLEARWEATHER = "ClearWeather"
const val COL_CARVESKILL = "CarveSkill"
const val COL_PARKSKILL = "ParkSkill"
const val COL_LEVELP = "ResortLocation"
const val COL_ID = "ID"

class PreferenceDatabaseHandler(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_POWDER + " INTEGER, " +
                COL_GROOMED + " INTEGER, " +
                COL_COLDWEATHER + " INTEGER, " +
                COL_WARMWEATHER + " INTEGER, " +
                COL_SNOWWEATHER + " INTEGER, " +
                COL_CLEARWEATHER + " INTEGER, " +
                COL_CARVESKILL + " INTEGER, " +
                COL_PARKSKILL + " INTEGER, " +
                COL_LEVELP + " INTEGER)"

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // modify user preference
    fun updatePreference(preference: Preference){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_POWDER,preference.powderSnow)
        cv.put(COL_GROOMED,preference.groomedSnow)
        cv.put(COL_COLDWEATHER,preference.coldWeather)
        cv.put(COL_WARMWEATHER,preference.warmWeather)
        cv.put(COL_SNOWWEATHER,preference.snowWeather)
        cv.put(COL_CLEARWEATHER,preference.clearWeather)
        cv.put(COL_CARVESKILL,preference.carveSkill)
        cv.put(COL_PARKSKILL,preference.parkSkill)
        cv.put(COL_LEVELP,preference.levelP)

        val success = db.update(TABLE_NAME, cv, "$COL_ID = ?", arrayOf("1")) > 0

        if (success)
            Toast.makeText(context, "Preferences Updated", Toast.LENGTH_SHORT).show()
        else    // row 1 data is empty
        {
            Toast.makeText(context, "Create New Preference", Toast.LENGTH_SHORT).show()
            val result = db.insert(TABLE_NAME,null,cv)  //insert new
            if(result == -1.toLong())
                Toast.makeText(context,"Failed to insert",Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(context,"Successful insertion",Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("Range")
    fun getPreference(): Preference {
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        val preference = Preference()

        if (cursor.moveToFirst()) {
            preference.powderSnow = cursor.getInt(cursor.getColumnIndex(COL_POWDER))
            preference.groomedSnow = cursor.getInt(cursor.getColumnIndex(COL_GROOMED))
            preference.coldWeather = cursor.getInt(cursor.getColumnIndex(COL_COLDWEATHER))
            preference.warmWeather = cursor.getInt(cursor.getColumnIndex(COL_WARMWEATHER))
            preference.snowWeather = cursor.getInt(cursor.getColumnIndex(COL_SNOWWEATHER))
            preference.clearWeather = cursor.getInt(cursor.getColumnIndex(COL_CLEARWEATHER))
            preference.carveSkill = cursor.getInt(cursor.getColumnIndex(COL_CARVESKILL))
            preference.parkSkill = cursor.getInt(cursor.getColumnIndex(COL_PARKSKILL))
            preference.levelP = cursor.getInt(cursor.getColumnIndex(COL_LEVELP))

        }
        cursor.close()
        return preference
    }

}