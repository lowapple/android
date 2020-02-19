package io.lowapple.sparta.git.app.repository

import io.lowapple.sparta.git.app.api.model.RepoCommit
import io.lowapple.sparta.git.app.api.service.GithubClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class GithubCommitRepositoryImpl(
    private val githubClient: GithubClient
) : GithubCommitRepository {
    override suspend fun commits(owner: String, repo: String): List<RepoCommit> {
        return withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            githubClient.commits(owner, repo)
        }
    }
}