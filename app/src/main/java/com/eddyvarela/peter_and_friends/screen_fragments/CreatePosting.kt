package com.eddyvarela.peter_and_friends.screen_fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import com.eddyvarela.peter_and_friends.R
import com.eddyvarela.peter_and_friends.data.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.create_posting_fragment.*
import java.util.*

class CreatePosting: DialogFragment(){

    companion object {
        const val TAG="Create"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.create_posting_fragment,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnSubmit.setOnClickListener {
            sendClick()
        }
    }

    fun sendClick(){
        uploadPost()
    }
    fun uploadPost(){
        val post = Post(
        FirebaseAuth.getInstance().currentUser!!.uid,
        "Peter",
        etJobTitle.text.toString(),
        etJobDescription.text.toString())

        var postCollection = FirebaseFirestore.getInstance().collection("posts")

        postCollection.add(post).addOnSuccessListener {
            Toast.makeText(activity, "Post saved successfully! ", Toast.LENGTH_LONG).show()
        }.addOnFailureListener{
            Toast.makeText(activity, "Post was not saved: ${it.message}", Toast.LENGTH_LONG).show()
        }
    }



//    override fun onCreateDialog(savedInstanceState: Bundle?) : Dialog {
//        val cal = Calendar.getInstance()
//        val year = cal.get(Calendar.YEAR)
//        val month = cal.get(Calendar.MONTH)
//        val day = cal.get(Calendar.DAY_OF_MONTH)
//
//        return DatePickerDialog(activity, this, year, month, day)
//    }

//
//    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//    }

}