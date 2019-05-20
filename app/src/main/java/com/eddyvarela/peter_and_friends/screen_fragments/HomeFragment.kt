package com.eddyvarela.peter_and_friends.screen_fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.eddyvarela.peter_and_friends.Adapter.PostAdapter
import com.eddyvarela.peter_and_friends.R
import com.eddyvarela.peter_and_friends.data.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {
    lateinit var postsAdapter: PostAdapter

    companion object {
        const val TAG = "Home_Fragment"
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        postsAdapter = PostAdapter(context!!, FirebaseAuth.getInstance().currentUser!!.uid)

        val layoutManager = LinearLayoutManager(context!!)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        recyclerPosts.layoutManager = layoutManager
        recyclerPosts.adapter = postsAdapter

        initPosts()
    }

    private fun initPosts() {

        val db = FirebaseFirestore.getInstance()

        val query = db.collection("posts")

        var allPostsListener = query.addSnapshotListener(
            object : EventListener<QuerySnapshot> {
                override fun onEvent(querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
                    if (e != null) {
                        Toast.makeText(context, "listen error: ${e.message}", Toast.LENGTH_LONG).show()
                        return
                    }

                    for (dc in querySnapshot!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
                                val post = dc.document.toObject(Post::class.java)
                                postsAdapter.addPost(post, dc.document.id)
                            }
                            DocumentChange.Type.MODIFIED -> {
                                Toast.makeText(context, "update: ${dc.document.id}", Toast.LENGTH_LONG)
                                    .show()
                            }
                            DocumentChange.Type.REMOVED -> {
                                postsAdapter.removePostByKey(dc.document.id)

                            }
                        }
                    }
                }
            }
        )
    }
}