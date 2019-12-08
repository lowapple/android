package io.lowapple.sparta.git.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import io.lowapple.sparta.git.app.api.model.AccessToken
import io.lowapple.sparta.git.app.api.service.GithubClient
import io.lowapple.sparta.git.app.repository.GithubClientRepository
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class AuthActivity : AppCompatActivity() {

    private lateinit var uri: String
    private val repository: GithubClientRepository by inject()
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        uri =
            "${AUTH_URL}client_id=${getString(R.string.sparta_github_client_id)}&redirect_uri=$REDIRECT_URI"

        if (Intent.ACTION_VIEW == intent.action) {
            val uri = intent.data
            if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {
                val code = uri.getQueryParameter("code")
                if (code != null) {
                    getAccessToken(code)
                }
            }
        } else {
            getUserCode()
        }
    }

    private fun getUserCode() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    uri
                )
            )
        )
    }

    private fun getAccessToken(code: String) {
        disposables.add(
            repository.getAccessToken(code)
                .subscribe {
                    // Token 저장
                    Preference.instance(applicationContext).edit()
                        .putString("token", it.access_token).apply()

                    disposables.add(
                        repository.getUser(it.access_token).subscribe {
                            Log.d(TAG, it.toString())
                        }
                    )

                    // 유저 등록
                    FirebaseAuth.getInstance()
                        .signInWithCredential(GithubAuthProvider.getCredential(it.access_token))
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(Intent(this@AuthActivity, CommitActivity::class.java))
                            } else {
                                Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            finish()
                        }
                }
        )
    }

    companion object {
        private const val TAG = "AuthActivity"
        private const val AUTH_URL = "https://github.com/login/oauth/authorize?"
        private const val REDIRECT_URI = "spartagit://authorize"
        public const val REQUEST = 10
        public const val SUCCESS = 1
        public const val FAILURE = 0
    }
}
