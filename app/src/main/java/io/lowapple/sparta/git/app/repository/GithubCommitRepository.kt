package io.lowapple.sparta.git.app.repository

import io.lowapple.sparta.git.app.api.model.RepoCommit
import io.reactivex.Observable

interface GithubCommitRepository {
    fun commits(owner: String, repo: String): Observable<List<RepoCommit>>
}