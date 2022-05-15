package co.id.rikihikmianto.paginationrecyclerview

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder().baseUrl("https://reqres.in/api/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        retrofit.create(ApiService::class.java)
    }
}