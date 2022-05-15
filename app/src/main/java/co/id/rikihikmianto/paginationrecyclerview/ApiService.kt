package co.id.rikihikmianto.paginationrecyclerview

import co.id.rikihikmianto.paginationrecyclerview.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {
    @GET("users")
    fun getUsers(
        @QueryMap hashMap: HashMap<String, String>
    ): Call<UserResponse>
}