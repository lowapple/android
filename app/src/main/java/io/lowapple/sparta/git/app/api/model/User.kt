package io.lowapple.sparta.git.app.api.model

data class User(
    val login: String,
    val id: Int,
    val avatar_url: String,
    val type: String,
    val site_admin: Boolean,
    val name: String,
    val company: String,
    val blog: String,
    val location: String,
    val email: String,
    val bio: String
)