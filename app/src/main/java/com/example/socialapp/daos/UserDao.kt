package com.example.socialapp.daos

import android.content.ContentValues.TAG
import android.os.Debug
import android.util.Log
import com.example.socialapp.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users") //Database ke andar multipe connections ho sakti hai lekin hmare ko users vaali chahie.

    fun addUser(user: User?) {
        //Ye kaam main thread pe ho raha hai lekin ham ni chahte ye main thread pe ho because nahi to utni der ke liye aapka ui block ho jaega.
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                usersCollection.document(user.uid).set(it) //ham chahte hai ki jo collection me user ki id bane vo user ki uid ke equal ho.
            }
        }
    }

    fun getUserById(uId: String): Task<DocumentSnapshot> {
        return usersCollection.document(uId).get() //Ye saare asynchronous task hote hai to ya to listeners use kar lo lekin ham coroutines use karenge aur async await use karenge.
        //get() function hame list laake dega mtlb task return karega.
        //document ke andar uId se user laake dega.
    }

}