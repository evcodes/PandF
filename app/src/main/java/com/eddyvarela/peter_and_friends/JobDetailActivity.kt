package com.eddyvarela.peter_and_friends

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.job_detail_layout.*
import kotlinx.android.synthetic.main.mail_row.*

class JobDetailActivity  : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_detail_layout)

        var jobID = intent.getStringExtra("jobID").toString()
        tvJobInfoTitle.text = intent.getStringExtra("jobTitle").toString()
        tvJobInfoDescription.text = intent.getStringExtra("jobDescription").toString()
        tvJobInfoAuthor.text = intent.getStringExtra("jobAuthor").toString()
        tvJobInfoPayAmt.text = intent.getStringExtra("jobPayAmt").toString()

        var imgUrl = intent.getStringExtra("imgUrl")

        if (imgUrl.isNotEmpty()) {
            ivJobDetails.visibility = View.VISIBLE
            Glide.with(this).load(imgUrl).into(ivJobDetails)
            //Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/peter-and-friends-ce246.appspot.com/o/images%2F06cb763d-47f3-4514-b9bb-34d3e6c276b7.jpg?alt=media&token=a8b466e7-a06d-472a-8920-c275fe691732").into(ivJobDetails)
        } else {
            ivJobDetails.visibility = View.GONE
        }

        btnJobApply.setOnClickListener {
            var time = intent.getStringExtra("time").toString()
            applyToJob(jobID, time)
        }
    }

    fun applyToJob(jobID: String, jobTime: String) {

        val writeMap = hashMapOf("username" to FirebaseAuth.getInstance().currentUser!!.displayName!!)
        val username: Map<String, String> = HashMap(writeMap)
        var db = FirebaseFirestore.getInstance()

        var s = db.collection("posts")
            .whereEqualTo("iod", jobID)
            .whereEqualTo("time", jobTime)
            .get()
            .addOnSuccessListener { documents ->

                for (doc in documents) {
                    if (doc != null) {

                        if (applyPossible(doc.id)) {
                            db.collection("posts").document(doc.id).collection("applicants")
                                .document(FirebaseAuth.getInstance().uid.toString())
                                .set(writeMap as Map<String, Any>)
                            Toast.makeText(this, "successfully applied to job", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(this, "cannot apply to job", Toast.LENGTH_LONG).show()

                        }
                    }
                }
            }
    }

    fun applyPossible(docID: String): Boolean {
        var applied = !checkIfApplied(docID)
        return applied
    }

    fun checkIfApplied(docID: String): Boolean {

        var applied = false
        var applicant =
            FirebaseFirestore.getInstance()
                .collection("posts")
                .document(docID)
                .collection("applicants")
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                .addSnapshotListener { documentSnapshot, e ->

                    Log.d("snap", documentSnapshot.toString())
                    Log.d("snap", e.toString())
                        if (documentSnapshot != null) {
//                        Toast.makeText(this, "Sorry, you cannot apply to a job more than once", Toast.LENGTH_LONG)
//                            .show()
                        applied = true
                    }
                }
        return applied
    }

    fun checkIfJobOwner(docID: String): Boolean {
        var iod = ""
        var applicant =
            FirebaseFirestore.getInstance().collection("posts")
                .document(docID).get()
                .addOnSuccessListener { document ->
                    iod = document.get("iod").toString()!!
                }
        var currentID = FirebaseAuth.getInstance().currentUser?.uid!!.toString()

        if (iod != currentID) {
            Toast.makeText(this, "this is not your job", Toast.LENGTH_LONG).show()
        }
        return (iod != currentID)
    }
}
