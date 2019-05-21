package com.eddyvarela.peter_and_friends

import android.os.Bundle
import android.widget.Toast
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import android.support.v7.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.UserProfileChangeRequest
import com.eddyvarela.peter_and_friends.data.UserInformation
import kotlinx.android.synthetic.main.user_registration_activity.*

private var mAuth: FirebaseAuth? = null

class RegisterActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_registration_activity)

        registerBtn.setOnClickListener {
            registerClick()
        }

    }
    private fun userNameFromEmail(email: String) = email.substringBefore("@")

    //check registration
    private fun registerClick() {
        if (!isFormValid()) {
            return
        }

        //create user
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            etEmailRegister.text.toString(), etPasswordRegister.text.toString()
        ).addOnSuccessListener {
            val user = it.user

            //update user profile
            user.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(userNameFromEmail(user.email!!))
                    .build()
            )

            //get users collection
            var userCollection = FirebaseFirestore.getInstance().collection("users")

            //collect users first and last name from input and give initial rating of 0.
            val userInfo = UserInformation(
                FirebaseAuth.getInstance().currentUser!!.uid,
                etFirstNameRegister.text.toString(),
                etLastNameRegister.text.toString(),
                0)

            //store users info in userCollection database
            userCollection.add(userInfo)//.addOnSuccessListener {
//                Toast.makeText(this@RegisterActivity, "User ", Toast.LENGTH_LONG).show()
//            }.addOnFailureListener{
//                Toast.makeText(this@RegisterActivity, "Post was not saved: ${it.message}", Toast.LENGTH_LONG).show()
//            }

            Toast.makeText(this@RegisterActivity,
                "Registration successful! Please log in now.", Toast.LENGTH_LONG).show()

            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))

        }.addOnFailureListener{
            Toast.makeText(this@RegisterActivity,
                "Registration failed. ${it.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun isFormValid(): Boolean {
        return when {
            etFirstNameRegister.text.isEmpty() ->{
                etFirstNameRegister.error = "Please input your first name!"
                false
            }
            etLastNameRegister.text.isEmpty() ->{
                etLastNameRegister.error = "Please input your last name!"
                false
            }
            etEmailRegister.text.isEmpty() -> {
                etEmailRegister.error = "Please input your email!"
                false
            }
            etPasswordRegister.text.isEmpty() -> {
                etPasswordRegister.error = "Please input your password!"
                false
            }
            etVerifyRegister.text.isEmpty() -> {
                etVerifyRegister.error = "Please retype your password!"
                false
            }
            (etPasswordRegister.text.toString() != etVerifyRegister.text.toString()) -> {
                etVerifyRegister.error = "Passwords do not match!"
                false
            }
            else -> true
        }
    }
}