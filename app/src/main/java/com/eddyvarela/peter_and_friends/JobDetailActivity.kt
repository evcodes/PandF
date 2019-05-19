package com.eddyvarela.peter_and_friends

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.job_detail_layout.*
import kotlinx.android.synthetic.main.mail_row.*

class JobDetailActivity: AppCompatActivity(){

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.job_detail_layout)


    tvJobInfoTitle.text= intent.getStringExtra("jobTitle").toString()
    tvJobInfoDescription.text= intent.getStringExtra("jobDescription").toString()
    tvJobInfoAuthor.text= intent.getStringExtra("jobAuthor").toString()
    tvJobInfoPayAmt.text= intent.getStringExtra("jobPayAmt").toString()
//    tvJobInfoAuthor.text= intent.getStringExtra("jobAuthor").toString()

    }
}