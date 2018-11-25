package com.example.daniel.meetkai_test.MeetKai

import okhttp3.ResponseBody
import retrofit2.http.*

interface HTTPInterface {
    /**
     * Request to create a user account
     */
    @FormUrlEncoded
    @POST("user")
    fun createUser(
            @Header("clientId")clientId : String,
            @Field("username") username: String,
            @Field("password") password : String,
            @Field("applicationSecret") applicationSecret : String?
    ) : retrofit2.Call<AuthenticationResponse>


    /**
     * Logs in a user
     */
    @GET("user")
    fun loginUser(
            @Header("clientId") clientId: String,
            @Header("username") username : String,
            @Header("password") password : String
    ) : retrofit2.Call<AuthenticationResponse>

    /**
     * Retrieves a random phrase from the server for the user to annotate
     */
    @GET("phrase")
    fun getPhrase(
            @Header("clientId") clientId: String,
            @Header("language") language : String,
            @Header("username") username : String,
            @Header("accessToken") accessToken : String
    ) : retrofit2.Call<GetPhraseResponse>

    /**
     * Allows the user to add a phrase to the server's database if it hasn't been added before
     */
    @FormUrlEncoded
    @POST("phrase")
    fun addPhrase(
            @Header("clientId") clientId : String,
            @Field("phrase") phrase: String,
            @Field("username") username : String,
            @Field("accessToken") accessToken: String
    ) : retrofit2.Call<ResponseBody>

    /**
     * Allows the user to refresh their access token
     */
    @FormUrlEncoded
    @PUT("user/token")
    fun renewToken(
            @Header("clientId") clientId: String,
            @Field("username") username : String,
            @Field("refreshToken") refreshToken : String
    ) : retrofit2.Call<AuthenticationResponse>

    /**
     * Allows the user to annotate a phrase
     */
    @FormUrlEncoded
    @PUT("phrase")
    fun annotatePhrase(
            @Header("clientId") clientId: String,
            @Field("username") username: String,
            @Field("accessToken") accessToken: String,
            @Field("hash") hash: String,
            @Field("languageAbr") languageAbr : String,
            @Field("isAzureCorrect") isAzureCorrect : Boolean,
            @Field("isGoogleCorrect") isGoogleCorrect : Boolean,
            @Field("isYandexCorrect") isYandexCorrect : Boolean
    ) : retrofit2.Call<ResponseBody>

    /**
     * Retrieves all of a user's annotations
     */
    @GET("user/annotations")
    fun retrieveUserAnnotations(
            @Header("clientId") clientId: String,
            @Header("username") username : String,
            @Header("accessToken") accessToken: String,
            @Header("targetUser") targetUser : String?
    ) : retrofit2.Call<ResponseBody>


    /**
     * Retrieves all of the annotations for a source text
     */
    @GET("phrase/annotations")
    fun retrieveSourceAnnotations(
            @Header("clientId") clientId : String,
            @Header("username") username : String,
            @Header("accessToken") accessToken : String,
            @Header("hash") hash : String?,
            @Header("phrase") phrase : String?
    ) :retrofit2.Call<ResponseBody>

    /**
     * Retrieves 50 source hash
     */
    @GET("phrase/hashes")
    fun retrieveSourceHashes(
            @Header("clientId") clientId: String,
            @Header("username") username : String,
            @Header("accessToken") accessToken: String
    ) : retrofit2.Call<SourceHashResponse>
}