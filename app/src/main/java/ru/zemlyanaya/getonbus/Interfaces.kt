package ru.zemlyanaya.getonbus

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.zemlyanaya.getonbus.mainactivity.model.Stops

interface IOnBackPressed {
    fun onBackPressed(): Boolean
}

interface IServerApi{
    @GET("/getRoute")
    fun getRoute(
        @Query("From") from: Int,
        @Query("To") to: Int,
        @Query("Allowed transport") transport: List<String>,
        @Query("Routing") mode: String
    ): Deferred<Response<List<String>>>

    @POST("/getNames")
    fun getNamesByID(@Query("Names") names: List<Int>): Deferred<Response<String>>

    @POST("/getStops")
    fun getAllStopsAsync(): Deferred<Response<Stops>>
}