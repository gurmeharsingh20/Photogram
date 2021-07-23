package com.example.socialapp

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView

class SplashScreen : AppCompatActivity() {

    private var TIME_OUT:Long = 10000
    lateinit var appLogo: ImageView
    lateinit var imageAnim: LottieAnimationView
    lateinit var topAnim: Animation
    lateinit var bottomAnim: Animation
    lateinit var fastBottomAnim: Animation
    lateinit var skipButton: TextView
    lateinit var nextButton: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        appLogo = findViewById(R.id.app_logo)
        imageAnim = findViewById(R.id.imageAnimation)
        skipButton = findViewById(R.id.skipButton)
        nextButton = findViewById(R.id.nextButton)

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        fastBottomAnim = AnimationUtils.loadAnimation(this, R.anim.fast_bottom_animation)

        supportActionBar?.hide()

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        appLogo.animation = topAnim
        imageAnim.animation = bottomAnim
        skipButton.animation = topAnim
        nextButton.animation = fastBottomAnim

        skipButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        nextButton.setOnClickListener {
            val intent = Intent(this, SplashScreen2::class.java)
            startActivity(intent)
        }

    }

//    private fun loadSplashScreen(){
//        Handler().postDelayed({
//            // You can declare your desire activity here to open after finishing splash screen. Like MainActivity
//            val intent = Intent(this,SignInActivity::class.java)
//            startActivity(intent)
//            finish()
//        },TIME_OUT)
//    }
}