package io.lowapple.sparta.git.app.repository

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.lowapple.sparta.git.app.api.model.AccessToken
import io.lowapple.sparta.git.app.api.model.User
import io.lowapple.sparta.git.app.api.service.GithubClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GithubClientRepositoryImpl(
    private val githubClientId: String,
    private val githubClientSecret: String,
    private val githubClient: GithubClient
) : GithubClientRepository {
    override suspend fun getAccessToken(code: String): AccessToken {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        return Retrofit
            .Builder()
            .baseUrl(GithubClient.BASE_URI)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(GithubClient::class.java)
            .getAccessToken(githubClientId, githubClientSecret, code)
    }

    override suspend fun getUser(token: String): User {
        return githubClient.getUser("bearer $token")
    }
}