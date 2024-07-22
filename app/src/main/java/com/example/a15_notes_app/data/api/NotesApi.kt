package com.example.a15_notes_app.data.api

import androidx.room.Delete
import com.example.a15_notes_app.data.model.Note
import okhttp3.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.example.a15_notes_app.data.model.NotesItem
import retrofit2.http.Body
import retrofit2.http.PUT

interface NotesApi {

    @GET("note/")
    suspend fun getNotes(): retrofit2.Response<ArrayList<NotesItem>>


    @POST("note/")
    suspend fun postNote(@Body note: Note): retrofit2.Response<NotesItem>

    @DELETE("note/{noteId}")
    suspend fun deleteNote(@Path("noteId") noteId:String): retrofit2.Response<NotesItem>

    @PUT("note/{noteId}")
    suspend fun updateNote(@Path("noteId") noteId:String, @Body note: Note): retrofit2.Response<NotesItem>
}