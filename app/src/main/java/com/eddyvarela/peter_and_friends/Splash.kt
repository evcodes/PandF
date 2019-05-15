package com.eddyvarela.peter_and_friends

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.splash_activity.*


class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

//        val showAnim = AnimationUtils.loadAnimation(this,
//            R.anim.btnanim)
//        animatedLogo.startAnimation(showAnim)

        Handler().postDelayed( {
            startActivity(Intent(this@Splash, LoginActivity::class.java))
            finish()
        }, 5000)

        // TODO: Change to 3000 in final phrase


    }
}
