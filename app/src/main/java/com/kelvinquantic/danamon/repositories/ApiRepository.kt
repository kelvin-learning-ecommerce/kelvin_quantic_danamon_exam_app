package com.kelvinquantic.danamon.repositories

import com.kelvinquantic.danamon.api.ApiService
import com.kelvinquantic.danamon.model.PhotoResponse
import com.kelvinquantic.danamon.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ApiRepository {
    fun getPhotoList(page: Int? = 1, limit: Int? = 10): Flow<Resource<List<PhotoResponse>>>

}

class ApiRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    ApiRepository {
    override fun getPhotoList(page: Int?, limit: Int?): Flow<Resource<List<PhotoResponse>>> {
        return apiService.getPhoto(page, limit)

    }


}
