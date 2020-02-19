package io.lowapple.sparta.git.app.repository

import io.lowapple.sparta.git.app.api.model.Repo
import io.reactivex.Flowable
import io.reactivex.Observable

interface GithubRepository {
    suspend fun repositories(token: String): List<Repo>
}