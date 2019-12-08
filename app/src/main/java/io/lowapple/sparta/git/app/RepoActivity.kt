package io.lowapple.sparta.git.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import io.lowapple.sparta.git.app.repository.GithubRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_repo.*
import org.koin.android.ext.android.inject

class RepoActivity : AppCompatActivity() {

    private lateinit var repoAdapter: RepoAdapter
    private val repository: GithubRepository by inject()
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo)

        if (FirebaseAuth.getInstance().currentUser != null) {
            val token = intent?.extras?.getString("token")
            if (token != null) {
                // Repository 정보 불러오기
                disposables.add(
                    repository
                        .repositories(token)
                        .subscribe {
                            Log.d(TAG, it.toString())
                        }
                )
            }
        } else {
            // Auth Activity 실행
            startActivity(
                Intent(applicationContext, AuthActivity::class.java)
            )
            finish()
        }

        repoAdapter = RepoAdapter()

        repo_recyclerview.apply {
            this.adapter = repoAdapter
            this.layoutManager = LinearLayoutManager(applicationContext)
        }
    }

    companion object {
        private val TAG = RepoActivity::class.java.simpleName
    }
}
