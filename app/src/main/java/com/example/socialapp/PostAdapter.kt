package com.example.socialapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostAdapter (options: FirestoreRecyclerOptions<Post>, val listener: IPostAdapter) : FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(options) {

    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val postText: TextView = itemView.findViewById(R.id.postTitle)
        val postImage:ImageView = itemView.findViewById(R.id.postImage)
        val userText: TextView = itemView.findViewById(R.id.userName)
        val createdAt: TextView = itemView.findViewById(R.id.createdAt)
        val likeCount: TextView = itemView.findViewById(R.id.likeCount)
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
        val commentButton: ImageView = itemView.findViewById(R.id.commentButton)
        val shareButton: ImageView = itemView.findViewById(R.id.shareButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val viewHolder = PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post,parent,false))
        viewHolder.likeButton.setOnClickListener {
            listener.onLikeClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id) //Ab ye hame post id chahie konsi post like ki hai to ab yahan kahi model to mil ni raha
            //to post id kaise milegi to ab post Id ke liye use karenge snapshots aur ye snapshots present hota hai Firestore Recycler View adapter me.
        }
        viewHolder.commentButton.setOnClickListener {
            listener.onCommentClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        viewHolder.shareButton.setOnClickListener {
            listener.onPostClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        //All data will be present in model and we will put that data in holder present in parameter in onBindViewHolder.
        holder.postText.text = model.text
        holder.userText.text = model.createdBy.displayName
        Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop().into(holder.userImage) //circleCrop() is function of Glide.
//        if(model.image == null){
//            holder.postImage.visibility = GONE
//        } else {
//            holder.postImage.visibility = VISIBLE
//        }
        Glide.with(holder.postImage.context).load(model.image).into(holder.postImage)
        holder.likeCount.text = model.likedBy.size.toString() //liked by ek array hai usme saare users ki id hongi aur uska size hame likeCount de dega.
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt) //created At long me hai millisecs me usko proper form me represent karne ke lie Utils bnaya hai.

        val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(currentUserId)
        if(isLiked) {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.ic_liked_icon))
        } else {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.ic_unlike_icon))
        }
    }
}



interface IPostAdapter {
    fun onLikeClicked(postId: String)
    fun onCommentClicked(id: String)
    fun onPostClicked(id:String)
}

