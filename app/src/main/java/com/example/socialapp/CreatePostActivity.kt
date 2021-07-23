package com.example.socialapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.socialapp.daos.PostDao
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_create_post.*


class CreatePostActivity : AppCompatActivity() {

    private lateinit var postDao: PostDao
    private lateinit var imageInput: ImageView
    private lateinit var imageInputButton: Button

    private val Tag = "FirebaseStorageManager"


    private lateinit var FilePathUri: Uri
    private val mStorageReference = FirebaseStorage.getInstance().reference
    private lateinit var mProgressDialog: ProgressDialog

    private var imageOutputUri: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        supportActionBar?.setTitle(Html.fromHtml("<font color='#000000'>Create Post</font>"))

        imageInput = findViewById(R.id.imageInput)
        imageInputButton = findViewById(R.id.imageInputButton)


        imageInputButton.setOnClickListener {
            pickImageFromGallery()
        }

        postDao = PostDao()
        postButton.setOnClickListener {
            val input = postInput.text.toString().trim()
//            val imageString = FilePathUri.toString()
            if (input.isNotEmpty()) {
                postDao.addPost(input,imageOutputUri)
                finish()
            }
        }
    }

//    private fun uploadFile() {
//        if(FilePathUri != null) {
//            var pd = ProgressDialog(this)
//            pd.setTitle("Uploading.....")
//            pd.show()
//
//            var imageRef = FirebaseStorage.getInstance().reference.child("images/pic.jpg")
//            imageRef.putFile(FilePathUri)
//                .addOnSuccessListener { p0 ->
//                    pd.dismiss()
//                    Toast.makeText(this,"File Uploaded Successfully",Toast.LENGTH_LONG).show()
//                }
//                .addOnFailureListener{ p0 ->
//                    pd.dismiss()
//                    Toast.makeText(this,p0.message,Toast.LENGTH_LONG).show()
//                }
//                .addOnProgressListener { p0 ->
//                    var progress = (100.0 * p0.bytesTransferred) / p0.totalByteCount
//                    pd.setMessage("Uploaded ${progress.toInt()}%")
//                }
//        }
//    }

    fun uploadImage(mContext: Context, imageUri: Uri) {
        mProgressDialog = ProgressDialog(mContext)
        mProgressDialog.setMessage("Please wait, image file being uploading......")
        mProgressDialog.show()
        val imageFileName = "images/pic${System.currentTimeMillis()}.png"
        val uploadTask = mStorageReference.child(imageFileName).putFile(imageUri)
        uploadTask.addOnSuccessListener {
            Log.e(Tag,"Image Uploaded Successfully")
            var downloadURLTask = mStorageReference.child(imageFileName).downloadUrl
            downloadURLTask.addOnSuccessListener {
//                Log.e(Tag,"Image Path : $it")
                imageOutputUri = "$it"
                mProgressDialog.dismiss()
            }
                .addOnFailureListener {
                    mProgressDialog.dismiss()
                }
        }.addOnFailureListener{
            Log.e(Tag,"Image Upload failed ${it.printStackTrace()}")
            mProgressDialog.dismiss()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent,"Choose Picture"),100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100  && resultCode == Activity.RESULT_OK && data != null) {
            FilePathUri = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver,FilePathUri)
            imageInput.setImageBitmap(bitmap)
            uploadImage(this,data.data!!)
        }
    }

}


