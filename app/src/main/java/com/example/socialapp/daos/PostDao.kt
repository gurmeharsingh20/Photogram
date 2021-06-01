package com.example.socialapp.daos

import com.example.socialapp.models.Post
import com.example.socialapp.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {
    val db = FirebaseFirestore.getInstance()
    val postCollections = db.collection("posts") //apne aap collection create bhi ho jaati hai agar nahi hai.
    val auth = Firebase.auth //currently signed in user ne hi create ki hogi post to vo get kar lenge ke kisne post create ki hai.

    fun addPost(text: String,image: String) {
        val currentUserId = auth.currentUser!!.uid //double exclamation ensure karta hai ki vo pakka null hai because bina sign in ke koi post create nahi kar sakta to isliye.
        //aur agar user null hua to vahan ye user id dhoondhne ki koshish karega aur null pointer exception aur app crash ho jaega.
        GlobalScope.launch {
            val userDao = UserDao()
            val user = userDao.getUserById(currentUserId).await().toObject(User::class.java)!! //task milega islie await lgaya hai aur await sirf coroutines me hi lga sakte hai.
            //object me convert karne ke lie toObject() use kra hai.

            val currentTime = System.currentTimeMillis()
            val post = Post(text, image, user, currentTime) //initially like vala to null hi hoga.
            postCollections.document().set(post)
        }
    }

    fun getPostById(postId: String): Task<DocumentSnapshot> {
        return postCollections.document(postId).get()
    }

    fun updateLikes(postId: String) {
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val post = getPostById(postId).await().toObject(Post::class.java) //task hai saath me toObject me convert kar denge.
            val isliked = post?.likedBy?.contains(currentUserId)

            if(isliked == true) {
                post?.likedBy?.remove(currentUserId)
            } else {
                post?.likedBy?.add(currentUserId)
            }
            post?.let { postCollections.document(postId).set(it) }
        }
    }
}