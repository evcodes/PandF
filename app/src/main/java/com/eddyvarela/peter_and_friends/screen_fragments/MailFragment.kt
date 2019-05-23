package com.eddyvarela.peter_and_friends.screen_fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.eddyvarela.peter_and_friends.Adapter.MailsAdapter
import com.eddyvarela.peter_and_friends.CreateMailActivity
import com.eddyvarela.peter_and_friends.R
import com.eddyvarela.peter_and_friends.data.Mail
import com.eddyvarela.peter_and_friends.data.ReceiverModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.apply_fragment.*
import kotlinx.android.synthetic.main.mail_fragment.*
import kotlinx.android.synthetic.main.mail_row.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Comparator


class MailFragment : Fragment() {


    companion object {
        const val TAG = "Mail_Fragment"
    }

    lateinit var mailsAdapter: MailsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.mail_fragment, container, false)

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }

//    var dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
//    override fun compare(o1: String?, o2: String?): Int {
//        return dateFormat.parse(o1).compareTo(dateFormat.parse(o2))
//    }



    private fun init() {
        fab.setOnClickListener { view ->
            startActivity(
                Intent(
                    this@MailFragment.context,
                    CreateMailActivity::class.java
                )
            )
        }


        // CHANGE HERE
        mailsAdapter = MailsAdapter(
            this.context!!,
            FirebaseAuth.getInstance().currentUser!!.email!!,
            FirebaseAuth.getInstance().currentUser!!.uid
        )

        val layoutManager = LinearLayoutManager(this.context)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mailsAdapter
        initMails()
    }

    private fun initMails() {
        val db = FirebaseFirestore.getInstance()

        //val query = db.collection("mails")

        // insert WHERE query here
        val from = db.collection("mails").whereEqualTo("uid", mailsAdapter.uId)

        val to = db.collection("mails").whereEqualTo("receiver", mailsAdapter.emailId)


        var allMailsListenerFrom = from.addSnapshotListener(
            object : EventListener<QuerySnapshot> {
                override fun onEvent(querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
                    if (e != null) {
                        Toast.makeText(this@MailFragment.context, "listen error: ${e.message}", Toast.LENGTH_LONG)
                            .show()
                        return
                    }

                    for (dc in querySnapshot!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
                                val mail = dc.document.toObject(Mail::class.java)
                                mailsAdapter.addMail(mail, dc.document.id)
                            }
                            DocumentChange.Type.MODIFIED -> {
                                Toast.makeText(
                                    this@MailFragment.context,
                                    "update: ${dc.document.id}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            DocumentChange.Type.REMOVED -> {
                                mailsAdapter.removeMailByKey(dc.document.id)
                            }
                        }
                    }
                }
            })


        var allMailsListenerTo = to.addSnapshotListener(
            object : EventListener<QuerySnapshot> {
                override fun onEvent(querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
                    if (e != null) {
                        Toast.makeText(this@MailFragment.context, "listen error: ${e.message}", Toast.LENGTH_LONG)
                            .show()
                        return
                    }

                    for (dc in querySnapshot!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
                                val mail = dc.document.toObject(Mail::class.java)
                                mailsAdapter.addMail(mail, dc.document.id)
                            }
                            DocumentChange.Type.MODIFIED -> {
                                Toast.makeText(
                                    this@MailFragment.context,
                                    "update: ${dc.document.id}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            DocumentChange.Type.REMOVED -> {
                                mailsAdapter.removeMailByKey(dc.document.id)
                            }
                        }
                    }
                }
            }
        )
    }
}