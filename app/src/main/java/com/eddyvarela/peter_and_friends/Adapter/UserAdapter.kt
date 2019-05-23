package com.eddyvarela.peter_and_friends.Adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.eddyvarela.peter_and_friends.EditBioActivity
import com.eddyvarela.peter_and_friends.R
import com.eddyvarela.peter_and_friends.data.UserInformation
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.account_fragment.view.*
import kotlinx.android.synthetic.main.account_information_row.*
import kotlinx.android.synthetic.main.account_information_row.view.*


class UserAdapter(

    private val context: Context, val uId: String) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {


    private var userList = mutableListOf<UserInformation>()
    private var userKeys = mutableListOf<String>()


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun getItemCount(): Int = userList.size

    //this is fine
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.account_information_row, parent, false
        )
        return ViewHolder(view)
    }


    fun addUser(UserInformation: UserInformation, key: String) {
        userList.add(UserInformation)
        userKeys.add(key)
        notifyDataSetChanged()
    }

    private fun removeUser(index: Int) {
        FirebaseFirestore.getInstance().collection("users").document(
            userKeys[index]
        ).delete()

        userList.removeAt(index)
        userKeys.removeAt(index)
        notifyItemRemoved(index)
    }

    fun removeUserByKey(key: String) {
        val index = userKeys.indexOf(key)
        if (index != -1) {
            userList.removeAt(index)
            userKeys.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {

        val (authorId, firstName, lastName, profilePicture, description, address, rating) =
            userList[holder.adapterPosition]

    }
}