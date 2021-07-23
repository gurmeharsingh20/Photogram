package com.example.socialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.daos.PostDao
import com.example.socialapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MyProfile : AppCompatActivity(), IMyPostAdapter {

    private lateinit var myProfileImage: ImageView
    private lateinit var myProfileName: TextView
    private lateinit var myProfileRecyclerView: RecyclerView
    private lateinit var myProfileAdapter: MyProfileAdapter
    private lateinit var postDao: PostDao
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    supportActionBar?.setTitle(Html.fromHtml("<font color='#000000'>My Profile</font>"))


        myProfileImage = findViewById(R.id.myProfileImage)
        myProfileName = findViewById(R.id.myProfileName)
        myProfileRecyclerView = findViewById(R.id.myProfileRecyclerView)

        val auth = Firebase.auth
        val userName = auth.currentUser!!.displayName
        val userImageURl = auth.currentUser!!.photoUrl
        val UID = auth.currentUser!!.uid

        Glide.with(myProfileImage.context).load(userImageURl).circleCrop().into(myProfileImage)
        myProfileName.text = userName.toString()

        postDao = PostDao()
        val postsCollections = postDao.postCollections
        val query = postsCollections.whereEqualTo("uid",UID)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()
        myProfileAdapter = MyProfileAdapter(recyclerViewOptions, this) //this ka mtlb intreface yehi activity implement kar rahi hai.
//
        myProfileRecyclerView.adapter = myProfileAdapter
        myProfileRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        myProfileAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        myProfileAdapter.stopListening()
    }

    override fun onDeleteClicked(postId: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Are You Sure, You Want To Delete!!")
        builder.setIcon(R.drawable.ic_delete_forever_24)
        builder.setNegativeButton("Yes") { dialogInterface, which->
            postDao.deletePost(postId)
        }
        builder.setPositiveButton("No") { dialogInterface, which->
            Toast.makeText(this,"No",Toast.LENGTH_SHORT).show();
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        true
    }
}

//        db.collection("posts")
//            .whereEqualTo("createdBy", true)
//            .get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
////                    val query = document.data?.get("posts")
//                    Log.d("SixerFour", "$document")
//                } else {
//                    Log.e("Values", "No such document")
//                }
//            }.addOnFailureListener { exception ->
//                Log.d("values", "get failed with ", exception)
//            }
//    }

//        val docRef = db.collection("posts").whereEqualTo("uid",UID)
//        docRef.get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    Log.d("SixerFour", "$document")
////                    myProfileAdapter = MyProfileAdapter(query as ArrayList<Map<String, String>>)
////                    postRecyclerView.adapter = adapter
////                    postRecyclerView.layoutManager = LinearLayoutManager(this)
//                } else {
//                    Log.e("Values", "No such document")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d("values", "get failed with ", exception)
//            }
//    }
