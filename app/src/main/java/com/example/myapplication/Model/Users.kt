package com.example.myapplication.Model

data class Users(
    var id_Utilisateur: Int,
    var username: String,
    var email: String,
    var password: String,
    var isAdmin: Boolean,
    var ville: String,
    var phone_Number: String,
    var street: String,
    var code_postal: Int
)
