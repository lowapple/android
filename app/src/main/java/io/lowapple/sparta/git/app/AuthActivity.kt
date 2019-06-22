package io.lowapple.sparta.git.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider
import android.content.Intent
import android.net.Credentials
import android.net.Uri
import android.os.AsyncTask
import com.google.android.gms.tasks.OnCompleteListener
import io.lowapple.sparta.git.app.api.model.AccessToken
import io.lowapple.sparta.git.app.api.service.GithubClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONTokener
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.net.URL


class AuthActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val AUTH_URL = "https://github.com/login/oauth/authorize?"
    private lateinit var authorizeUrl: String
    private val redirectUrl = "spartagit://authorize"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val clientId = "client_id=" + getString(R.string.sparta_github_client_id)
        authorizeUrl = "$AUTH_URL$clientId&redirect_uri=$redirectUrl"
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser == null) {
            getUserCode()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Intent.ACTION_VIEW == intent.action) {
            val uri = intent.data
            if (uri != null && uri.toString().startsWith(redirectUrl)) {
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
                    authorizeUrl
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
            .baseUrl("https://github.com/")
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
                    auth.signInWithCredential(GithubAuthProvider.getCredential(this))
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "success")
                            } else {
                                Log.d(TAG, "failure")
                            }
                        }
                }
            }

            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }

    companion object {
        private const val TAG = "AuthActivity"
    }
}
