package com.geodeveloper.mydiary

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_entry_design.*


class SignUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_design)
        header_display_textView.text = "Set Password"
        btn_signIn.text = "Sign up"
        checkToSignIn()
    }


    fun btnSelected(view: View) {
        val btnSelected = view as Button
        when (btnSelected) {
            btn0_sn -> sign_text_input.append(btn0_sn.text.toString())
            btn1_sn -> sign_text_input.append(btn1_sn.text.toString())
            btn2_sn -> sign_text_input.append(btn2_sn.text.toString())
            btn3_sn -> sign_text_input.append(btn3_sn.text.toString())
            btn4_sn -> sign_text_input.append(btn4_sn.text.toString())
            btn5_sn -> sign_text_input.append(btn5_sn.text.toString())
            btn6_sn -> sign_text_input.append(btn6_sn.text.toString())
            btn7_sn -> sign_text_input.append(btn7_sn.text.toString())
            btn8_sn -> sign_text_input.append(btn8_sn.text.toString())
            btn9_sn -> sign_text_input.append(btn9_sn.text.toString())
            btnClear_sn -> sign_text_input.setText("")
        }
        vibrate()
    }

    fun checkToSignIn() {
        val dbManag = validationDbManager(this)
        var password = ""
        val projections = arrayOf("id","password")
        val selectionArgs = arrayOf("%")
        val cursor = dbManag.query(projections, "password like ?", selectionArgs, "id")
        if (cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndex("password"))
        }
        if (password.isNotEmpty()) {
            val i = Intent(this, SignIn::class.java)
            startActivity(i)
            finish()
        }
    }

    fun signing(view: View) {
        if (sign_text_input.length() > 0) {
            val dbManager = validationDbManager(this)
            val myValues = ContentValues()
            myValues.put("password", sign_text_input.text.toString())
             val addToDatabase = dbManager.insert(myValues)

            if (addToDatabase > 0) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            }
            finish()

        } else {
            Toast.makeText(this, "Please enter some values", Toast.LENGTH_LONG).show()
        }
    }
    inner class validationDbManager(context: Context) {
        val dbName = "userData"
        val dbTable = "signup"
        val colID = "id"
        val colPassword = "password"
        val dbVersion = 1

        //This create  database table with name and with three columns (colID as integer and the primary key, the tile column as text and description as also text)
        val createSignUpTable = "CREATE TABLE IF NOT EXISTS $dbTable($colID INTEGER PRIMARY KEY,$colPassword TEXT);"
        var myDataBase: SQLiteDatabase? = null

        init {
            var db = sqlDatabaseHelper(context)
            myDataBase = db.writableDatabase
        }
        inner class sqlDatabaseHelper(context: Context) :
            SQLiteOpenHelper(context, dbName, null, dbVersion) {
            override fun onCreate(p0: SQLiteDatabase?) {
                p0?.execSQL(createSignUpTable)
            }
            override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
                p0?.execSQL("Drop table IF EXISTS $dbTable")
            }
        }
        fun insert(values: ContentValues): Long {
            val ID = myDataBase!!.insert(dbTable, "", values)
            return ID
        }
        fun query(projection: Array<String>, selection: String, selectionArg: Array<String>, sortOrder: String): Cursor {
            val qb = SQLiteQueryBuilder()
            qb.tables = dbTable
            val cursor =
                qb.query(myDataBase, projection, selection, selectionArg, null, null, sortOrder)
            return cursor
        }

        // A function that update the existing value on the database
        fun update(values: ContentValues, selections: String, selectionArg: Array<String>): Int {

            val count = myDataBase!!.update(dbTable, values, selections, selectionArg)
            return count

        }


    }

    fun vibrate(){
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        }else{
            vibrator.vibrate(50)
        }
    }
}
