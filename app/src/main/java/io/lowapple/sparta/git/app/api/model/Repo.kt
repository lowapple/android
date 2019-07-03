package io.lowapple.sparta.git.app.api.model

data class Repo(
    val id: String,
    val name: String,
    val full_name: String,
    val private: Boolean,
    val description: String,
    val fork: Boolean,
    val created_at: String,
    val updated_at: String,
    val html_url: String,
    val owner: Owner
) {
    data class Owner(
        val avatar_url: String
    )
}