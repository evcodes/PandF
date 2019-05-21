package com.eddyvarela.peter_and_friends.Adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
//import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.eddyvarela.peter_and_friends.R
import com.eddyvarela.peter_and_friends.data.Mail
import kotlinx.android.synthetic.main.mail_row.*
import kotlinx.android.synthetic.main.mail_row.view.*
import java.util.*
import kotlin.Comparator
import java.text.SimpleDateFormat


class MailsAdapter (
    val context: Context,
    val emailId: String,
    val uId: String) : RecyclerView.Adapter<MailsAdapter.ViewHolder>(), Comparator<String> {

    var dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

    override fun compare(o1: String?, o2: String?): Int {
        return dateFormat.parse(o1).compareTo(dateFormat.parse(o2))
    }

    private var mailsList = mutableListOf<Mail>()
    private var mailsKey = mutableListOf<String>()

    private var isImageFitToScreen: Boolean = false

    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.mail_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount() =  mailsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (authorId, date, author, receiver, title, body, imgUrl) =
            mailsList[holder.adapterPosition]

        holder.tvDate.text = "Date: " + date

        if (emailId == author) {
            holder.tvAuthor.text = "Author: You"
            holder.tvReceiver.text = "Receiver: " + receiver
            val spearMint = context.getResources().getColor(R.color.colorSpearmint)
            holder.rowMail.setBackgroundColor(spearMint)
        }
        else if (emailId == receiver) {
            holder.tvReceiver.text = "Receiver: You"
            holder.tvAuthor.text = "Author: " + author
            val accent = context.getResources().getColor(R.color.colorAccent)
            holder.rowMail.setBackgroundColor(accent)
        }

        holder.tvTitle.text = "Title: " + title
        holder.tvBody.text = "Body: " + body

        if (uId == authorId) {
            holder.btnDeleteMail.visibility = View.VISIBLE

            holder.btnDeleteMail.setOnClickListener {
                removeMail(holder.adapterPosition)
            }


        } else {
            holder.btnDeleteMail.visibility = View.GONE
        }

//        if (imgUrl.isNotEmpty()) {
//            holder.ivPhoto.visibility = View.VISIBLE
//            Glide.with(context).load(imgUrl).into(holder.ivPhoto)
//        } else {
//            holder.ivPhoto.visibility = View.GONE
//        }

        setAnimation(holder.itemView, position)

        holder.ivPhoto.setOnClickListener(View.OnClickListener {
            if(isImageFitToScreen) {
                isImageFitToScreen=false;
                holder.ivPhoto.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                holder.ivPhoto.setAdjustViewBounds(true)
            } else {
                isImageFitToScreen=true;
                holder.ivPhoto.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                holder.ivPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        })

        mailsList.sortedWith(compareBy {holder.tvDate.toString() })


    }

    fun addMail(Mail: Mail, key: String) {
        mailsList.add(Mail)
        mailsKey.add(key)
        notifyDataSetChanged()

    }

    private fun removeMail(index: Int) {
        FirebaseFirestore.getInstance().collection("mails").document(
            mailsKey[index]
        ).delete()

        mailsList.removeAt(index)
        mailsKey.removeAt(index)
        notifyItemRemoved(index)
    }

    fun removeMailByKey(key: String) {
        val index = mailsKey.indexOf(key)
        if (index != -1) {
            mailsList.removeAt(index)
            mailsKey.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context,
                android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rowMail: LinearLayout = itemView.rowMail
        val tvDate: TextView = itemView.tvDate
        val tvAuthor: TextView = itemView.tvAuthor
        val tvReceiver: TextView = itemView.tvReceiver
        val tvTitle: TextView = itemView.tvTitle
        val tvBody: TextView = itemView.tvBody
        val btnDeleteMail: Button = itemView.btnDeleteMail
        val ivPhoto: ImageView = itemView.ivPhoto
    }
}