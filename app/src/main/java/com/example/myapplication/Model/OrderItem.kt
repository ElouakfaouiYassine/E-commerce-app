package com.example.myapplication.Model

import android.net.Uri

data class OrderItem (
    val id: Long,
    val image: String ,
    val name: String,
    val description: String,
    val price: Double,
    val price_promotion: Double)

