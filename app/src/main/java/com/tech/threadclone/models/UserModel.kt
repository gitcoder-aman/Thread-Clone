package com.tech.threadclone.models
data class UserModel(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val bio: String = "",
    val userName: String = "",
    val imageUrl: String = "",
    val uid: String? = ""
)
