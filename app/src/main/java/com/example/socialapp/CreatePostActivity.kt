package com.example.socialapp

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socialapp.daos.PostDao
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.android.synthetic.main.activity_create_post.*


class CreatePostActivity : AppCompatActivity() {

    private lateinit var postDao: PostDao
    private lateinit var imageInput: ImageView
    private lateinit var imageInputButton: Button

    private lateinit var FilePathUri: Uri

    private val PICK_IMAGE_CODE = 0
    private val PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        imageInput = findViewById(R.id.imageInput)
        imageInputButton = findViewById(R.id.imageInputButton)

        imageInputButton.setOnClickListener {
            pickImageFromGallery()
        }

        postDao = PostDao()
        postButton.setOnClickListener {
            val input = postInput.text.toString().trim()
            val imageString = FilePathUri.toString()
            if (input.isNotEmpty()) {
                postDao.addPost(input,imageString)
                finish()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,PICK_IMAGE_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                }
                else {
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_CODE) {
            FilePathUri = data?.data!!
            imageInput.setImageURI(data?.data)
        }
    }

}


