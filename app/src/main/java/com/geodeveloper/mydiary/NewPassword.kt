package com.geodeveloper.mydiary

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_entry_design.*
import kotlinx.android.synthetic.main.custom_dialogue.view.*

class NewPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_entry_design)
        header_display_textView.text = "New Password"
        btn_signIn.text = "OK"
        btn_signIn.setOnClickListener {
            setNewPassword("%")
        }
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

    fun setNewPassword(title: String) {
        val dbManag = SignUp().validationDbManager(this)
        val myValues = ContentValues()
        myValues.put("password", sign_text_input.text.toString())

        val selectionArgs = arrayOf("1")
        val addDataToDataBase = dbManag.update(myValues, "id=?", selectionArgs)
        if (addDataToDataBase > 0) {

            val mDialogueView = LayoutInflater.from(this).inflate(R.layout.custom_dialogue, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogueView)
            mDialogueView.emoji_icon.setImageResource(R.drawable.hugging_face_emoji)
            mDialogueView.header_textView.setTextColor(Color.parseColor("#DA1212"))
            mDialogueView.dialogue_cancel.text = "Home"
            mDialogueView.header_textView.text = "Successfully updated"
            mDialogueView.dialogue_ok.text =""
            val mAlertDualogue = mBuilder.show()
            mDialogueView.dialogue_cancel.setOnClickListener {
                mAlertDualogue.dismiss()
                val intent  = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        } else {
            Toast.makeText(this, "Error occur", Toast.LENGTH_LONG).show()
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
