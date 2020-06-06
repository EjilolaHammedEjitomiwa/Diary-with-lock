package com.geodeveloper.mydiary

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager(
    //Database details
    val context: Context,
    val dbName: String = "myNotes",
    val dbTable: String = "notes",
    val colId: String = "ID",
    val colTitle: String = "title",
    val colDesc: String = "description",
    val colDate:String = "date",
    val version: Int = 1
) {
    /** Sqlite statement to create tables
     *  "CREATE TABLE IF NOT EXISTS notes (ID INTEGER  PRIMARY KEY, title TEXT, description TEXT, date TEXT);"
     * **/

    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS $dbTable ($colId INTEGER  PRIMARY KEY, $colTitle TEXT, $colDesc TEXT, $colDate TEXT);"
    //instance of the database class
    private var sqlDb: SQLiteDatabase? = null

    init {
        val db = SqlDataBaseHelper(context)
        sqlDb = db.writableDatabase
    }

    //Databasehelper class which inherit from the SqliteopenHelper class
    inner class SqlDataBaseHelper(val context: Context) : SQLiteOpenHelper(context, dbName, null, version) {
        //A function which create the database
        override fun onCreate(p0: SQLiteDatabase?) {
            p0!!.execSQL(sqlCreateTable)
        }
        //upgrade function which drop the existing version of the database, if the version changes
        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0!!.execSQL("Drop table IF EXISTS $dbTable")
        }

    }

    // A function to insert values  to the database
    fun insert(values: ContentValues): Long {
        val id = sqlDb!!.insert(dbTable, "", values)
        return id
    }

    // A function to query the database
    fun query(
        projection: Array<String>,
        selection: String,
        selectionArg: Array<String>,
        sortOrder: String
    ): Cursor {

        val qb = SQLiteQueryBuilder()
        qb.tables = dbTable
        val cursor = qb.query(sqlDb, projection, selection, selectionArg, null, null, sortOrder)
        return cursor
    }

    // A function that delete data from the database
    fun delete(selection: String, selectionArg: Array<String>): Int {

        val count = sqlDb!!.delete(dbTable, selection, selectionArg)
        return count

    }

    // A function that update the existing value on the database
    fun update(values: ContentValues, selections: String, selectionArg: Array<String>): Int {

        val count = sqlDb!!.update(dbTable, values, selections, selectionArg)
        return count

    }

}