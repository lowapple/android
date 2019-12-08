package io.lowapple.sparta.git.app.repository

import io.lowapple.sparta.git.app.api.model.RepoCommit
import io.lowapple.sparta.git.app.api.service.GithubClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class GithubCommitRepositoryImpl(
    private val githubClient: GithubClient
) : GithubCommitRepository {
    override fun commits(owner: String, repo: String): Observable<List<RepoCommit>> {
        return githubClient.commits(owner, repo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}