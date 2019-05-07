package com.eddyvarela.peter_and_friends

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_activity.*


private var mAuth: FirebaseAuth? = null

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login_activity)
        FirebaseApp.initializeApp(this)

        mAuth = FirebaseAuth.getInstance()


        registerButton.setOnClickListener {
            registerUser()
        }

        loginButton.setOnClickListener {
            loginUser()
        }

    }

    private fun loginUser() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            etEmail.text.toString(), etPassword.text.toString()
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))

            } else {
                Toast.makeText(
                    this@LoginActivity, "Error: " +
                            it.exception?.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.addOnFailureListener {
            Toast.makeText(
                this@LoginActivity,
                "Error: ${it.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun registerUser() {
        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
    }

}