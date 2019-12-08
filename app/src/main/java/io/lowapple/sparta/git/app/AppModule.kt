package io.lowapple.sparta.git.app

import android.content.Context
import io.lowapple.sparta.git.app.api.service.GithubClient
import io.lowapple.sparta.git.app.repository.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun provideInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return interceptor
}

fun provideApiClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
    OkHttpClient().newBuilder().addInterceptor(interceptor).build()

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder().baseUrl("https://api.github.com/")
        .client(okHttpClient)
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

fun provideRegisterService(retrofit: Retrofit): GithubClient =
    retrofit.create(GithubClient::class.java)

val networkModules = module {
    factory { provideInterceptor() }
    factory { provideApiClient(get()) }
    factory { provideRegisterService(get()) }
    single { provideRetrofit(get()) }
}

val viewModelModules = module {

}

val repositoryModules = module {
    factory<GithubRepository> { GithubRepositoryImpl(get()) }
    factory<GithubCommitRepository> { GithubCommitRepositoryImpl(get()) }
    factory<GithubClientRepository> {
        GithubClientRepositoryImpl(
            get<Context>().getString(R.string.sparta_github_client_id),
            get<Context>().getString(R.string.sparta_github_client_secret),
            get()
        )
    }
}

val appModule = listOf(networkModules, viewModelModules, repositoryModules)