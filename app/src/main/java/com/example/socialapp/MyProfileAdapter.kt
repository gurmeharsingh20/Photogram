package com.example.socialapp

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

class MyProfileAdapter (options: FirestoreRecyclerOptions<Post>, val listener: IMyPostAdapter): FirestoreRecyclerAdapter<Post, MyProfileAdapter.MyProfileViewHolder>(options) {

    class MyProfileViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val postText: TextView = itemView.findViewById(R.id.postTitleProfile)
        val postImage: ImageView = itemView.findViewById(R.id.postImageProfile)
        val userText: TextView = itemView.findViewById(R.id.myName)
        val createdAt: TextView = itemView.findViewById(R.id.createdAtProfile)
        val userImage: ImageView = itemView.findViewById(R.id.myImage)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProfileViewHolder {
        val viewHolder = MyProfileViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.myprofile_post, parent, false)
        )

        viewHolder.deleteButton.setOnClickListener {
            listener.onDeleteClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyProfileViewHolder, position: Int, model: Post) {
        Log.d("OK Bhai!!!!!!!!!", "$model.text")
        holder.postText.text = model.text
        holder.userText.text = model.createdBy.displayName
        Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop().into(holder.userImage)
        Glide.with(holder.postImage.context).load(model.image).circleCrop().into(holder.postImage)
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)
    }
}

interface IMyPostAdapter {
    fun onDeleteClicked(postId: String)
}