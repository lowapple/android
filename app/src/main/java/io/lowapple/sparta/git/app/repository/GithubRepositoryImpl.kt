package io.lowapple.sparta.git.app.repository

import io.lowapple.sparta.git.app.api.model.Repo
import io.lowapple.sparta.git.app.api.service.GithubClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubRepositoryImpl(
    private val githubClient: GithubClient
) : GithubRepository {
    override suspend fun repositories(token: String): List<Repo> {
        return githubClient.repositories("bearer $token", "owner")
    }
}