package io.lowapple.sparta.git.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import io.lowapple.sparta.git.app.api.model.Repo
import io.lowapple.sparta.git.app.api.service.GithubClient
import kotlinx.android.synthetic.main.activity_repo.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RepoActivity : AppCompatActivity() {

    private lateinit var repoAdapter: RepoAdapter

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

        repoAdapter = RepoAdapter()

        repo_recyclerview.apply {
            this.adapter = repoAdapter
            this.layoutManager = LinearLayoutManager(applicationContext)
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
                response.body()?.apply {
                    repoAdapter.update(this)
                }
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {

            }
        })
    }

    companion object {
        private val TAG = RepoActivity::class.java.simpleName
    }
}
