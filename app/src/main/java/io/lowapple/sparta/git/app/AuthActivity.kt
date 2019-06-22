package io.lowapple.sparta.git.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
        setContentView(R.layout.activity_main)

        uri = "${AUTH_URL}client_id=${getString(R.string.sparta_github_client_id)}&redirect_uri=$REDIRECT_URI"
    }

    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser == null) {
            getUserCode()
        } else {

        }
    }

    override fun onResume() {
        super.onResume()
        if (Intent.ACTION_VIEW == intent.action) {
            val uri = intent.data
            if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {
                val code = uri.getQueryParameter("code")
                if (code != null) {
                    getAccessToken(code)
                }
            }
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
                token?.apply {
                    FirebaseAuth.getInstance().signInWithCredential(GithubAuthProvider.getCredential(this))
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
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
    }
}
