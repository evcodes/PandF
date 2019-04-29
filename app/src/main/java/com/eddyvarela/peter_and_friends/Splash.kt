package com.eddyvarela.peter_and_friends

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.annotation.UiThread
import java.lang.Thread.sleep
import java.util.*
import kotlin.concurrent.thread
import android.support.v4.os.HandlerCompat.postDelayed



class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        Handler().postDelayed( {
            val i = Intent(this@Splash, MainActivity::class.java)
            startActivity(i)
            finish()
        }, 3000)


    }
}
