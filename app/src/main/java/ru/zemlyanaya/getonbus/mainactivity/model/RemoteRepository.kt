package ru.zemlyanaya.getonbus.mainactivity.model

import ru.zemlyanaya.getonbus.App.Companion.api


class RemoteRepository: BaseRemoteRepository(){

//     suspend fun getAllStops(): MutableList<String?>? {
//        val stopsRespond = safeApiCall(
//            call = { api.getAllStopsAsync().await() },
//            errorMessage = "Ошибка при получении списка остановок! Ничего не будет работать!"
//        )
//
//        return stopsRespond?.getStopsList()
//    }

    suspend fun getPosts() : List<PlaceholderPosts>? {
        return safeApiCall(
        call = { api.getPostsAsync().await() },
        errorMessage = "")
    }
}