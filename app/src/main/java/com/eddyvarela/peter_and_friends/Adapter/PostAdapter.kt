package com.eddyvarela.peter_and_friends.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eddyvarela.peter_and_friends.R
import com.eddyvarela.peter_and_friends.data.Post
import kotlinx.android.synthetic.main.job_post_row.view.*

class PostAdapter(
    private val context: Context, private val uId: String) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private var postsList = mutableListOf<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.job_post_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount() = postsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (authorId, author, title, pay) =
            postsList[holder.adapterPosition]

        holder.tvAuthor.text = author
        holder.tvTitle.text = title
        holder.tvPay.text = pay

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.tvJobTitle
        val tvAuthor = itemView.tvAuthor
        val tvPay = itemView.tvJobPay

    }
}