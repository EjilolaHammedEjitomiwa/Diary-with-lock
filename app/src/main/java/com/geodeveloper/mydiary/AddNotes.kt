package com.geodeveloper.mydiary

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*
import kotlinx.android.synthetic.main.activity_splash_screen.*
import kotlinx.android.synthetic.main.custom_dialogue.view.*
import java.text.SimpleDateFormat
import java.util.*

class AddNotes : AppCompatActivity() {
    //initialize id to 0
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)
        checkUpdates()
    }

    fun checkUpdates() {
        //to recieve intent extras from the MainActivity (id)
        id = intent.getIntExtra("noteId", 0)

        if (id != 0) {
            //set the title and content editText to the intent extras value for update
            title_input_field.setText(intent.getStringExtra("noteTitle"))
            content_input_field.setText(intent.getStringExtra("noteDescription"))
        }
    }

    fun addNotes(view: View) {
        //Instance of the DbManager class
        val dbManager = DbManager(this)
        //To get the current date and time on the users phone and format it
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        //adding data to contentValues in keys and values
        val myValues = ContentValues()
        myValues.put("title", title_input_field.text.toString())
        myValues.put("description", content_input_field.text.toString())
        myValues.put("date", currentDate)

        //user must enter title
        if (title_input_field.text.isNotEmpty()) {

            //if id == 0 it means its a new note therefore insert function is perform
            if (id == 0) {
                /**call the insert function in the database class
                 * Note: insert function return a long value to clerify if data has been entered or not
                 * **/
                val addDataToDataBase = dbManager.insert(myValues)
                //verify if data has been successfully entered
                if (addDataToDataBase > 0) {
                    //display sucess emoji
                    addSucessDisplay()
                } else {
                    Toast.makeText(this, "ERROR OCCUR", Toast.LENGTH_LONG).show()
                }
            } else {
                //but if id != 0 i.e has some value then perform update function, also update function return  a int value
                val selectionArgs = arrayOf(id.toString())
                val addDataToDataBase = dbManager.update(myValues, "ID=?", selectionArgs)
                if (addDataToDataBase > 0) {
                    updateSucessDisplay()
                } else {
                    Toast.makeText(this, "ERROR OCCUR", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_LONG).show()
        }
        vibrate()
    }

    //sucess emoji diplay
    fun addSucessDisplay() {
        val mDialogueView = LayoutInflater.from(this).inflate(R.layout.custom_dialogue, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogueView)
        mDialogueView.emoji_icon.setImageResource(R.drawable.hugging_face_emoji)
        mDialogueView.header_textView.setTextColor(Color.parseColor("#083836"))
        mDialogueView.dialogue_cancel.text = "Cancel"
        mDialogueView.header_textView.text = "Note added successfully"
        mDialogueView.dialogue_ok.text = "Home"
        val mAlertDualogue = mBuilder.show()
        mDialogueView.dialogue_cancel.setOnClickListener {
            mAlertDualogue.dismiss()
        }
        mDialogueView.dialogue_ok.setOnClickListener{
            mAlertDualogue.dismiss()
            finish()
        }
    }

    fun updateSucessDisplay() {
        val mDialogueView = LayoutInflater.from(this).inflate(R.layout.custom_dialogue, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogueView)
        mDialogueView.emoji_icon.setImageResource(R.drawable.hugging_face_emoji)
        mDialogueView.header_textView.setTextColor(Color.parseColor("#083836"))
        mDialogueView.dialogue_cancel.text = "Cancel"
        mDialogueView.header_textView.text = "Note Updated successfully"
        mDialogueView.dialogue_ok.text = "Home"
        val mAlertDualogue = mBuilder.show()
        mDialogueView.dialogue_cancel.setOnClickListener {
            mAlertDualogue.dismiss()
        }
        mDialogueView.dialogue_ok.setOnClickListener{
            mAlertDualogue.dismiss()
            finish()
        }
    }

    fun vibrate() {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(50)
        }
    }

}
