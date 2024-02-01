package com.kelvin.pastisystem.network

import com.kelvin.pastisystem.api.MovieApi
import com.kelvin.pastisystem.api.MovieService
import com.kelvin.pastisystem.repositories.MovieRepository
import com.kelvin.pastisystem.repositories.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(ViewModelComponent::class)
object RetrofitModule {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    @Provides
    @ViewModelScoped
    fun provideRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client =

            OkHttpClient.Builder().apply {
                addInterceptor(
                    Interceptor { chain ->
                        val builder = chain.request().newBuilder()
                        builder.header(
                            "Authorization",
                            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlMmNkYzk4N2VkYTVhOGQ2NTY5NzJhMDFkMGU4YTRjNSIsInN1YiI6IjY1YjhkMGI0OTBmY2EzMDE3YjA2YjJjYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.spVYpTYrG5KfJxtN-LG9LZiM-jk_If6w9x4jB9aRMzc"
                        )

                        return@Interceptor chain.proceed(builder.build())
                    }
                )
                addInterceptor(interceptor)
            }.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Games
    @Provides
    @ViewModelScoped
    fun provideMovieApi(retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideMovieService(gameApi: MovieApi): MovieService {
        return MovieService(gameApi)  // Or however you create an instance of GameService
    }

    @Provides
    @ViewModelScoped
    fun provideMovieRepository(gameService: MovieService): MovieRepository {
        return MovieRepositoryImpl(gameService)  // Or however you create an instance of GameRepository
    }
}
