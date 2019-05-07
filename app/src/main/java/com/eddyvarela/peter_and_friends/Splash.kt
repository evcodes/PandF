package com.eddyvarela.peter_and_friends

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler


class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        Handler().postDelayed( {
            startActivity(Intent(this@Splash, LoginActivity::class.java))
            finish()
        }, 3000)

        // TODO: Change to 3000 in final phrase


    }
}
