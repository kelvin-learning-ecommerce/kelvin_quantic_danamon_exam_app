package com.kelvinquantic.danamon.di

import com.kelvinquantic.danamon.api.ApiInterface
import com.kelvinquantic.danamon.api.ApiService
import com.kelvinquantic.danamon.repositories.ApiRepository
import com.kelvinquantic.danamon.repositories.ApiRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(ViewModelComponent::class)
object DaggerHiltModule {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    @Provides
    @ViewModelScoped
    fun provideRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client =

            OkHttpClient.Builder().apply {
                addInterceptor(interceptor)
            }.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @ViewModelScoped
    fun provideMovieApi(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideMovieService(apiInterface: ApiInterface): ApiService {
        return ApiService(apiInterface)
    }

    @Provides
    @ViewModelScoped
    fun provideMovieRepository(apiService: ApiService): ApiRepository {
        return ApiRepositoryImpl(apiService)
    }
}
