package io.lowapple.sparta.git.app.repository

import io.lowapple.sparta.git.app.api.model.AccessToken
import io.lowapple.sparta.git.app.api.model.User
import io.reactivex.Observable

interface GithubClientRepository {
    suspend fun getAccessToken(code: String): AccessToken
    suspend fun getUser(token: String): User
}