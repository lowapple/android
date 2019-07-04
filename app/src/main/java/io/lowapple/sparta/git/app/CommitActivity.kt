package io.lowapple.sparta.git.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.lowapple.sparta.git.app.api.model.RepoCommit
import io.lowapple.sparta.git.app.api.service.GithubClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CommitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commit)

        val pref = Preference.instance(this)
        val token = pref.getString("token", "")
        if (token != "") {
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
            api.commits("lowapple", "sparta-git-app").enqueue(object : Callback<List<RepoCommit>> {
                override fun onFailure(call: Call<List<RepoCommit>>, t: Throwable) {

                }

                override fun onResponse(call: Call<List<RepoCommit>>, response: Response<List<RepoCommit>>) {
                   
                }
            })
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
