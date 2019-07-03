package io.lowapple.sparta.git.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider
import com.google.gson.JsonObject
import io.lowapple.sparta.git.app.api.model.AccessToken
import io.lowapple.sparta.git.app.api.model.Repo
import io.lowapple.sparta.git.app.api.service.GithubClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RepoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo)

        if (FirebaseAuth.getInstance().currentUser != null) {
            getRepos()
        } else {
            // Auth Activity 실행
            startActivity(
                Intent(applicationContext, AuthActivity::class.java)
            )
            finish()
        }
    }

    private fun getRepos() {
        val token = intent.extras.getString("token")

        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val api = retrofit.create(GithubClient::class.java)
        api.repos("bearer $token", "owner").enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                Log.d(TAG, response.body().toString())
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {

            }
        })
        /*
        FirebaseAuth.getInstance()
            .currentUser
            ?.getIdToken(true)
            ?.addOnCompleteListener { task ->
                task.result?.apply {

                }
            }
        */
    }

    companion object {
        private val TAG = RepoActivity::class.java.simpleName
    }
}
