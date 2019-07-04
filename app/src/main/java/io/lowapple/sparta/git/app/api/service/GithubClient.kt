package io.lowapple.sparta.git.app.api.service

import com.google.gson.JsonObject
import io.lowapple.sparta.git.app.api.model.AccessToken
import io.lowapple.sparta.git.app.api.model.Repo
import io.lowapple.sparta.git.app.api.model.RepoCommit
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface GithubClient {

    @Headers("Accept: application/json")
    @POST("/login/oauth/access_token")
    @FormUrlEncoded
    fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ): Call<AccessToken>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("/user/repos")
    fun repos(
        @Header("Authorization") accessToken: String,
        @Query("affiliation") affiliation: String
    ): Call<List<Repo>>

    @GET("/repos/{owner}/{repo}/commits")
    fun commits(
        @Path("owner") owner: String, @Path("repo") repo: String
    ): Call<List<RepoCommit>>

    companion object {
        const val BASE_URI = "https://github.com/"
    }
}