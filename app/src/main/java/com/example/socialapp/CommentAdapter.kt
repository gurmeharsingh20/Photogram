package com.example.socialapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CommentAdapter(private val commentsList : ArrayList<Map<String, String>>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userImageComment: ImageView = itemView.findViewById(R.id.userImageComment)
        val userNameComment: TextView = itemView.findViewById(R.id.userNameComment)
        val creationTime: TextView = itemView.findViewById(R.id.timeComment)
        val commentDescription: TextView = itemView.findViewById(R.id.commentDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):CommentAdapter.CommentViewHolder {
        val viewHolder = CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_comment,parent,false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentViewHolder, position: Int) {
//        Log.e("Op Bhai","All Values:- ${commentsList[position]["uid"]}")
        //All data will be present in model and we will put that data in holder present in parameter in onBindViewHolder.
        Glide.with(holder.userImageComment.context).load(commentsList[position]["imageURl"]).circleCrop().into(holder.userImageComment) //circleCrop() is function of Glide.
        holder.userNameComment.text = commentsList[position]["displayName"]
        var time: Long? = commentsList[position]["CreatedAt"]?.toLong()
        holder.creationTime.text = time?.let { Utils.getTimeAgo(it) }
        holder.commentDescription.text = commentsList[position]["comments_post"]
    }

    override fun getItemCount(): Int {
        return commentsList.size
    }
}
