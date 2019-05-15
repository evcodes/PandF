package com.eddyvarela.peter_and_friends.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eddyvarela.peter_and_friends.R
import com.eddyvarela.peter_and_friends.data.Post
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.job_post_row.view.*

class PostAdapter(
    private val context: Context, private val uId: String) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private var postsList = mutableListOf<Post>()
    private var postKeys = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.job_post_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount() = postsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (authorId, author, title, description) =
            postsList[holder.adapterPosition]

        holder.tvAuthor.text = author
        holder.tvTitle.text = title
        holder.tvDescription.text = description

    }

    fun removePost(index:Int){
        FirebaseFirestore.getInstance().collection("posts").document(
            postKeys[index]
        ).delete()

        postsList.removeAt(index)
        postKeys.removeAt(index)
        notifyItemRemoved(index)
    }

    fun removePostByKey(key:String){
        val index = postKeys.indexOf(key)
        if (index != -1) {
            postsList.removeAt(index)
            postKeys.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun addPost(post: Post, key: String) {
        postsList.add(post)
        postKeys.add(key)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.tvJobTitle
        val tvAuthor:TextView = itemView.tvAuthor
        val tvDescription:TextView = itemView.tvDescription

    }
}