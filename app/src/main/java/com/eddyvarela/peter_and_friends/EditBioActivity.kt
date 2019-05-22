package com.eddyvarela.peter_and_friends

import android.os.Bundle
import android.widget.Toast
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import android.support.v7.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.UserProfileChangeRequest
import com.eddyvarela.peter_and_friends.data.UserInformation
import com.eddyvarela.peter_and_friends.screen_fragments.AccountFragment
import com.google.android.gms.tasks.Tasks
import kotlinx.android.synthetic.main.user_registration_activity.*
import kotlinx.android.synthetic.main.user_update_info_activity.*

private var mAuth: FirebaseAuth? = null

class EditBioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_update_info_activity)

        btnUpdateInfo.setOnClickListener {
            updateInfoClick()
        }

    }


    private fun updateInfoClick() {

        val writeFirstName  = hashMapOf("username" to etUpdateFirstName.text.toString())
        val firstName: Map<String,String> = HashMap(writeFirstName)

        val writeLastName  = hashMapOf("username" to etUpdateLastName.text.toString())
        val lastName: Map<String,String> = HashMap(writeLastName)

        val writeUpdateDesc  = hashMapOf("username" to etUpdateDesc.text.toString())
        val desc: Map<String,String> = HashMap(writeUpdateDesc)

        val writeUpdateAddress  = hashMapOf("username" to etUpdateAddress.text.toString())
        val address: Map<String,String> = HashMap(writeUpdateAddress)



        var userInfo = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("iod", FirebaseAuth.getInstance().currentUser!!.uid).get()
            .addOnSuccessListener {doc->
                for (d in doc){
                    if(!etUpdateFirstName.text.isEmpty()){
                        FirebaseFirestore.getInstance().collection("users").document(d.id).update(firstName)
                    }
                    if(!etUpdateLastName.text.isEmpty()){
                        FirebaseFirestore.getInstance().collection("users").document(d.id).update(lastName)
                    }

                    if(!etUpdateDesc.text.isEmpty()){
                        FirebaseFirestore.getInstance().collection("users").document(d.id).update(desc)
                    }
                    if(!etUpdateAddress.text.isEmpty()){
                        FirebaseFirestore.getInstance().collection("users").document(d.id).update(address)
                    }
                }

            }




                    startActivity(Intent(this@EditBioActivity, AccountFragment::class.java))

                }

            }