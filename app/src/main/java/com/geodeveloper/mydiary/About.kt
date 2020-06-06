package com.geodeveloper.mydiary

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import kotlinx.android.synthetic.main.activity_about.*

class About : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
//on click listener to twitter icon
        twitter_icon.setOnClickListener{
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://twitter.com/ejilolahmed?s=09")
            startActivity(openURL)
            vibrate()
        }
//on click listener to whatsapp icon
        whatsapp_icon.setOnClickListener{
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://wa.me/2348080357062")
            startActivity(openURL)
            vibrate()
        }
        //on click listener to instagram icon
        insta_icon.setOnClickListener{
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://wwww.instagram.com/ejilola_hammed_ejitomiwa")
            startActivity(openURL)
            vibrate()
        }

    }

//A function to vibrate phone
    fun vibrate(){
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50,VibrationEffect.DEFAULT_AMPLITUDE))
        }else{
            vibrator.vibrate(50)
        }
    }

}
