package io.lowapple.sparta.git.app.api.model

import java.util.*

data class RepoCommit(
    val sha: String,
    val commit: Commit
) {
    data class Commit(
        val author: Author,
        val committer: Committer,
        val message: String
    ) {
        data class Author(
            val name: String,
            val email: String,
            val date: Date
        )

        data class Committer(
            val name: String,
            val email: String,
            val date: Date
        )
    }
}