package io.lowapple.sparta.git.app.api.service

import io.lowapple.sparta.git.app.api.model.AccessToken
import retrofit2.Call
import retrofit2.http.*

interface GithubClient {

    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ): Call<AccessToken>

    @GET("login/{user}/repos")
    fun repos(@Path("user") user: String)
}