package com.kelvinquantic.danamon.api

import com.kelvinquantic.danamon.model.PhotoResponse
import com.kelvinquantic.danamon.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException
import javax.inject.Inject

interface ApiInterface {
    @GET("photos/")
    suspend fun getPhoto(
        @Query("_page") page: Int?, @Query("_limit") limit: Int?
    ): Response<List<PhotoResponse>>
}

class ApiService @Inject constructor(private val api: ApiInterface) {
    fun getPhoto(page: Int?, limit: Int?): Flow<Resource<List<PhotoResponse>>> = flow {
        emit(Resource.Loading())
        try {
            val genre = api.getPhoto(page, limit)

            emit(Resource.Success(genre.body()))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "Oops, something went wrong!"
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server, check your internet connection."
                )
            )
        }
    }
}
