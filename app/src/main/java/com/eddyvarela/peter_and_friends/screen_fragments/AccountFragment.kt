package com.eddyvarela.peter_and_friends.screen_fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eddyvarela.peter_and_friends.Adapter.UserAdapter
import com.eddyvarela.peter_and_friends.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import com.eddyvarela.peter_and_friends.EditBioActivity
import com.eddyvarela.peter_and_friends.data.UserInformation


import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.account_fragment.*
import kotlinx.android.synthetic.main.account_information_row.*
import kotlinx.android.synthetic.main.create_posting_fragment.*


class AccountFragment: Fragment() {

    lateinit var userAdapter: UserAdapter


    companion object {
        const val TAG="Account_Fragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.account_fragment,container,false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userAdapter = UserAdapter(context!!, FirebaseAuth.getInstance().currentUser!!.uid)

        val layoutManager = LinearLayoutManager(context!!)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        userAccountView.layoutManager = layoutManager
        userAccountView.adapter = userAdapter

        initUserProfile()

    }

    private fun toEditBioActivity() {
        startActivity(Intent(context, EditBioActivity::class.java))
    }

    private fun initUserProfile(){

        val database = FirebaseFirestore.getInstance()
        val account = database.collection("users").whereEqualTo("iod", userAdapter.uId)

        var accountListenerFrom = account.addSnapshotListener(
            object: EventListener<QuerySnapshot> {
                override fun onEvent(querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
                    if (e != null) {
                        Toast.makeText(context, "listen error: ${e.message}", Toast.LENGTH_LONG).show()
                        return
                    }
                    for (dc in querySnapshot!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
                                val post = dc.document.toObject(UserInformation::class.java)
                                userAdapter.addUser(post, dc.document.id)
                            }
                            DocumentChange.Type.MODIFIED -> {
                                Toast.makeText(context, "update: ${dc.document.id}", Toast.LENGTH_LONG)
                                    .show()
                            }
                            DocumentChange.Type.REMOVED -> {
                                userAdapter.removeUserByKey(dc.document.id)

                            }
                        }
                    }
                }
            }
        )
    }
}