package io.lowapple.sparta.git.app.api.service

import com.google.gson.JsonObject
import io.lowapple.sparta.git.app.api.model.AccessToken
import io.lowapple.sparta.git.app.api.model.Repo
import io.lowapple.sparta.git.app.api.model.RepoCommit
import io.lowapple.sparta.git.app.api.model.User
import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface GithubClient {

    /**
     * Github 의 AccessToken 을 가져오는 API
     */
    @Headers("Accept: application/json")
    @POST("/login/oauth/access_token")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ): AccessToken

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("/user")
    suspend fun getUser(
        @Header("Authorization") accessToken: String
    ): User


    /**
     * Github 의 사용자 Repositories 를 얻어오는 API
     */
    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("/user/repos")
    suspend fun repositories(
        @Header("Authorization") accessToken: String,
        @Query("affiliation") affiliation: String
    ): List<Repo>

    /**
     * Github 의 사용자 Commit 목록을 얻어오는 API
     */
    @GET("/repos/{owner}/{repo}/commits")
    suspend fun commits(
        @Path("owner") owner: String, @Path("repo") repo: String
    ): List<RepoCommit>

    companion object {
        const val BASE_URI = "https://github.com/"
    }
}