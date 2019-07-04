package io.lowapple.sparta.git.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import io.lowapple.sparta.git.app.api.model.AccessToken
import io.lowapple.sparta.git.app.api.service.GithubClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class AuthActivity : AppCompatActivity() {

    private lateinit var uri: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        uri = "${AUTH_URL}client_id=${getString(R.string.sparta_github_client_id)}&redirect_uri=$REDIRECT_URI"

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
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(GithubClient.BASE_URI)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val api = retrofit.create(GithubClient::class.java)
        api.getAccessToken(
            getString(R.string.sparta_github_client_id),
            getString(R.string.sparta_github_client_secret),
            code
        ).enqueue(object : Callback<AccessToken> {
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                val token = response.body()?.access_token
                if (token != null) {
                    Preference.instance(applicationContext).edit().putString("token", token).apply()
                    FirebaseAuth.getInstance().signInWithCredential(GithubAuthProvider.getCredential(token))
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                            }
                            finish()
                        }
                } else {
                    Preference.instance(applicationContext).edit().putString("token", "").apply()
                }
                finish()
            }

            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
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
