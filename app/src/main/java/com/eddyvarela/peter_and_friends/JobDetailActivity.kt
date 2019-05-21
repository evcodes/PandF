package com.eddyvarela.peter_and_friends

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.job_detail_layout.*
import kotlinx.android.synthetic.main.mail_row.*

class JobDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_detail_layout)

        var jobID = intent.getStringExtra("jobID").toString()
        tvJobInfoTitle.text = intent.getStringExtra("jobTitle").toString()
        tvJobInfoDescription.text = intent.getStringExtra("jobDescription").toString()
        tvJobInfoAuthor.text = intent.getStringExtra("jobAuthor").toString()
        tvJobInfoPayAmt.text = intent.getStringExtra("jobPayAmt").toString()

    btnJobApply.setOnClickListener {
        var time = intent.getStringExtra("time").toString()
            applyToJob(jobID,time)
        }
    }

    fun applyToJob(jobID:String,jobTime:String){


        val writeMap  = hashMapOf("username" to "eddy")
        val username: Map<String,String> = HashMap(writeMap)
        var db = FirebaseFirestore.getInstance()

        var s = db.collection("posts")
            .whereEqualTo("iod",jobID)
            .whereEqualTo("time", jobTime)
            .get()
            .addOnSuccessListener { documents ->

                for (doc in documents){
                    if (doc != null) {
                        db.collection("posts").document(doc.id).collection("applicants").add(username)
                        Toast.makeText(this,doc.id,Toast.LENGTH_LONG).show()


                    }
                }
        }
    }

//        var job= db
//            .collection("posts").document(jobID)
//            .collection("applicants")

//        Toast.makeText(this,,Toast.LENGTH_LONG).show()


    }
