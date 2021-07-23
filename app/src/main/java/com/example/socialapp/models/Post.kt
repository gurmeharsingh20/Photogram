package com.example.socialapp.models

import com.google.common.collect.ArrayListMultimap

data class Post(
    val text: String = "",
    val image: String? = "",
    val createdBy: User = User(), //kisne create ki thi
    val createdAt: Long = 0L, //kab create hui thi
    val uid: String = "",
    val likedBy: ArrayList<String> = ArrayList(),
    val comments: ArrayList<Map<String,String>> = ArrayList<Map<String,String>>())

