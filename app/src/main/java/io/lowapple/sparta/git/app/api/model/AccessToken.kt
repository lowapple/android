package io.lowapple.sparta.git.app.api.model

import com.google.gson.annotations.JsonAdapter

data class AccessToken(
    val access_token: String,
    val token_type: String
)