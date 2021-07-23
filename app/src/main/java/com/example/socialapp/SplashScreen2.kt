package com.example.socialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView

class SplashScreen2 : AppCompatActivity() {


    lateinit var bottomAnim: Animation
    lateinit var googleSignIn: TextView
    lateinit var nextButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen2)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        supportActionBar?.hide()
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        googleSignIn = findViewById(R.id.googleSignIn)

        nextButton = findViewById(R.id.nextButton2)

        googleSignIn.animation = bottomAnim
        nextButton.animation = bottomAnim

        nextButton.setOnClickListener {
            val intent = Intent(this, SplashScreen3::class.java)
            startActivity(intent)
        }

    }
}