package ru.zemlyanaya.getonbus.mainactivity.model

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory


class RemoteRepository: BaseRemoteRepository(){

    private val tempApi: PlaceholderApi = Retrofit.Builder()
        .client(OkHttpClient().newBuilder().build())
        .baseUrl("https://jsonplaceholder.typicode.com")
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(PlaceholderApi::class.java)

//       val serverApi = Retrofit.Builder()
//            .client(OkHttpClient().newBuilder().build())
//            .baseUrl("https://unknown")
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
//            .addConverterFactory(JacksonConverterFactory.create())
//            .build()
//            .create(IServerApi::class.java)

//     suspend fun getAllStops(): MutableList<String?>? {
//        val stopsRespond = safeApiCall(
//            call = { api.getAllStopsAsync().await() },
//            errorMessage = "Ошибка при получении списка остановок! Ничего не будет работать!"
//        )
//
//        return stopsRespond?.getStopsList()
//    }

    suspend fun getPosts() : List<PlaceholderPosts>? = safeApiCall(
        call = { tempApi.getPostsAsync().await() },
        errorMessage = "Не удалось подключиться к серверу!")
}