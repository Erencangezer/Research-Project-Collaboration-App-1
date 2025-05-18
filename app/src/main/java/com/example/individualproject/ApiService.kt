package com.example.individualproject

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.*




data class MessageRequest(
    val research_id: Int,
    val sender_email: String,
    val receiver_email: String?, // grup mesajıysa null
    val content: String
)

data class MessageResponse(
    val success: Boolean,
    val message: String
)

data class MessageItem(
    val sender_email: String,
    val receiver_email: String?, // grup mesajında null gelir
    val content: String,
    val sent_at: String
)

data class MessageListItem(
    val title: String,
    val isGroup: Boolean,
    val researchId: Int?, // Grup sohbeti için
    val receiverEmail: String? // Kişisel sohbet için
)

data class MessageListResponse(
    val success: Boolean,
    val messages: List<MessageItem>
)

data class GroupMessageRequest(
    val research_id: Int,
    val sender_email: String,
    val content: String
)

data class SimpleResponse(
    val success: Boolean,
    val message: String
)


interface ApiService {

    @POST("login")
    fun login(@Body user: User): Call<LoginResponse>

    @POST("register")
    fun register(@Body user: User): Call<RegisterResponse>

    @GET("getUserByEmail")
    fun getUserByEmail(@Query("email") email: String): Call<UserResponse>

    @POST("addResearch")
    fun addResearch(@Body request: AddResearchRequest): Call<DefaultResponse>

    @GET("get_researches")
    fun getResearches(@Query("email") email: String): Call<ResearchListResponse>

    @POST("/addNote")
    fun addNote(
        @Body request: AddNoteRequest
    ): Call<GenericResponse>

    @GET("/getNotes")
    fun getNotes(
        @Query("research_id") researchId: Int
    ): Call<NoteListResponse>


    @POST("sendMessage")
    fun sendMessage(@Body message: MessageRequest): Call<MessageResponse>

    @GET("getGroupMessages")
    fun getGroupMessages(@Query("research_id") researchId: Int): Call<MessageListResponse>



    @GET("getPrivateMessages")
    fun getPrivateMessages(
        @Query("research_id") researchId: Int,
        @Query("sender_email") senderEmail: String,
        @Query("receiver_email") receiverEmail: String
    ): Call<List<MessageItem>>





}

