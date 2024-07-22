package com.example.a15_notes_app.di

import com.example.a15_notes_app.data.api.AuthInterceptor
import com.example.a15_notes_app.data.api.NotesApi
import com.example.a15_notes_app.data.api.UserApi
import com.example.a15_notes_app.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit.Builder
    {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }



    @Provides
    @Singleton
    fun provideUserApi(retrofitBuilder :Retrofit.Builder):UserApi
    {
        return retrofitBuilder.build().create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(interceptor: AuthInterceptor):OkHttpClient
    {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Provides
    @Singleton
    fun provideNotesApi(retrofitBuilder: Retrofit.Builder, okHttpCl:OkHttpClient): NotesApi{
        return retrofitBuilder.client(okHttpCl).build().create(NotesApi::class.java)
    }

}



