package com.example.socialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialapp.daos.PostDao
import com.example.socialapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_comment.*

class CommentActivity : AppCompatActivity() {

    private lateinit var postDao: PostDao
    private lateinit var adapter: CommentAdapter
    private val db = FirebaseFirestore.getInstance()
    private lateinit var postRecyclerView: RecyclerView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        supportActionBar?.setTitle(Html.fromHtml("<font color='#000000'>Comments</font>"))
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


        val Postid:String = intent.getStringExtra("postID").toString()

        postDao = PostDao()
        floatingCommentButton.setOnClickListener {
            val commentInput = textComment.text.toString().trim()
            if(commentInput.isNotEmpty()) {
                postDao.addComments(Postid,commentInput)
                finish()
            }
        }

        postRecyclerView = findViewById(R.id.postRecyclerView)
//
        val docRef = db.collection("posts").document(Postid.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val query = document.data?.get("comments")
                    adapter = CommentAdapter(query as ArrayList<Map<String, String>>)
                    postRecyclerView.adapter = adapter
                    postRecyclerView.layoutManager = LinearLayoutManager(this)
                } else {
                    Log.e("Values", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("values", "get failed with ", exception)
            }
//        postRecyclerView = findViewById(R.id.postRecyclerView)
//        postDao = PostDao()
//        val postsCollections = postDao.postCollections.document(Postid.toString())
//        val query = postsCollections.document("comments")
//        val query = postsCollections.orderBy("comments",Query.Direction.DESCENDING)
//        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()
//        adapter = CommentAdapter(recyclerViewOptions) //this ka mtlb intreface yehi activity implement kar rahi hai.
//
//        postRecyclerView.adapter = adapter
//        postRecyclerView.layoutManager = LinearLayoutManager(this)

    }


//    override fun onStart() {
//        super.onStart()
//        adapter.startListening()
//       }
//
//    override fun onStop() {
//        super.onStop()
//        adapter.stopListening()
//  }
}

