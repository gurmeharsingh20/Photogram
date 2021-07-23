package com.example.socialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView

class SplashScreen3 : AppCompatActivity() {

    lateinit var bottomAnim: Animation
    lateinit var postAndComment: TextView
    lateinit var nextButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen3)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        postAndComment = findViewById(R.id.postAndComment)

        nextButton = findViewById(R.id.nextButton3)

        postAndComment.animation = bottomAnim
        nextButton.animation = bottomAnim

        nextButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}