package io.lowapple.sparta.git.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.lowapple.sparta.git.app.repository.GithubCommitRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class CommitActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()
    private val repository: GithubCommitRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commit)

        val pref = Preference.instance(this)
        val token = pref.getString("token", "")
        if (token != "") {
            CoroutineScope(Dispatchers.Main).launch {
                repository.commits("lowapple", "sparta-git-app").apply {
                    Log.d(TAG, this.toString())
                }
            }
        } else {
            startActivity(Intent(applicationContext, AuthActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    companion object {
        val TAG = CommitActivity::class.java.simpleName
    }
}
