package com.example.socialapp

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.socialapp.daos.PostDao
import com.example.socialapp.daos.UserDao
import com.example.socialapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.view.*

class MainActivity : AppCompatActivity(), IPostAdapter {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var postDao: PostDao
    private lateinit var adapter: PostAdapter
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var currentUserImage: ImageView
    private lateinit var currentUserName: TextView
    private lateinit var hView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        supportActionBar?.setTitle(Html.fromHtml("<font color='#000000'>Photogram</font>"))

        floatingAddButton.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }

        //Drawer Code

        val auth = Firebase.auth
        val UserName = auth.currentUser!!.displayName
        val UserImageURl = auth.currentUser!!.photoUrl
        navigationView = findViewById(R.id.navigation_view)
        hView = navigationView.inflateHeaderView(R.layout.nav_header)
        currentUserImage = hView.findViewById(R.id.currentUserImage)
        currentUserName = hView.findViewById(R.id.currentUserName)
        Glide.with(currentUserImage.context).load(UserImageURl).circleCrop().into(currentUserImage)
        currentUserName.text = UserName.toString()
        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout,R.string.open,R.string.close)
        toggle.drawerArrowDrawable.setColor(getResources().getColor(R.color.black))
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.editProfile -> {
                    val intent = Intent(this,MyProfile::class.java)
                    startActivity(intent)
                }
                R.id.logout -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Logout")
                    builder.setMessage("Are you sure, You want to Logout!!")
                    builder.setIcon(R.drawable.ic_baseline_logout_24)
                    builder.setNegativeButton("Yes") {dialogInterface, which ->
                        FirebaseAuth.getInstance().signOut()
                        finish()
                        val intent = Intent(this,SignInActivity::class.java)
                        startActivity(intent)
                    }
                    builder.setPositiveButton("No") {dialogInterface, which ->
                        Toast.makeText(this,"No",Toast.LENGTH_LONG).show();
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    true
                }
                R.id.enableNightMode -> {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                    setTheme(R.style.DarkTheme)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        setUpRecyclerView()

    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.menu_item, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.logout -> {
//                val builder = AlertDialog.Builder(this)
//                builder.setTitle("Logout")
//                builder.setMessage("Are you sure, You want to Logout!!")
//                builder.setIcon(R.drawable.ic_baseline_logout_24)
//                builder.setNegativeButton("Yes") {dialogInterface, which ->
//                    FirebaseAuth.getInstance().signOut()
//                    finish()
//                    val intent = Intent(this,SignInActivity::class.java)
//                    startActivity(intent)
//                }
//                builder.setPositiveButton("No") {dialogInterface, which ->
//                    Toast.makeText(this,"No",Toast.LENGTH_LONG).show();
//                }
//                val alertDialog: AlertDialog = builder.create()
//                alertDialog.setCancelable(false)
//                alertDialog.show()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpRecyclerView() { //Isko chahie ek adapter aur layout manager.
        postDao = PostDao()
        val postsCollections = postDao.postCollections
        val query = postsCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()
        adapter = PostAdapter(recyclerViewOptions, this) //this ka mtlb intreface yehi activity implement kar rahi hai.

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }

    override fun onCommentClicked(id: String) {
        val intent = Intent(this, CommentActivity::class.java)
        intent.putExtra("postID",id)
        startActivity(intent)
    }


    override fun onPostClicked(id: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"    //if you are sharing image then .jpg if video then mp3.
        val text =
            "https://console.firebase.google.com/u/0/project/social-app-48aa2/firestore/data~2Fposts~$id"
        intent.putExtra(Intent.EXTRA_TEXT,text)
        val chooser = Intent.createChooser(intent,"Share this meme using.....")
        startActivity(chooser)
    }
}