package com.example.socialapp.models

data class Post(
    val text: String = "",
    val image: String = "",
    val createdBy: User = User(), //kisne create ki thi
    val createdAt: Long = 0L, //kab create hui thi
    val likedBy: ArrayList<String> = ArrayList())