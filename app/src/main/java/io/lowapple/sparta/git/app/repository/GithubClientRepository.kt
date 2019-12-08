package io.lowapple.sparta.git.app.repository

import io.lowapple.sparta.git.app.api.model.AccessToken
import io.lowapple.sparta.git.app.api.model.User
import io.reactivex.Observable

interface GithubClientRepository {
    fun getAccessToken(code: String): Observable<AccessToken>
    fun getUser(token: String): Observable<User>
}