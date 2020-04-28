package ru.zemlyanaya.getonbus.mainactivity.data.api


import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.zemlyanaya.getonbus.mainactivity.data.model.StopsRespond

interface IApiService {

    @GET("/getRoute")
    suspend fun getRoute(
        @Query("From") from: Int,
        @Query("To") to: Int,
        @Query("Allowed transport") transport: List<String>,
        @Query("Routing") mode: String
    ): List<String>

    @GET("/getNext")
    suspend fun getNextInstruction(@Query("number") number: Int): String

    @GET("/getNext")
    suspend fun getNextInstruction(): List<String>

    @POST("/getNames")
    suspend fun getNamesByID(@Query("Names") names: List<Int>): List<String>

    @POST("/getStops")
    suspend fun getAllStopsAsync(): StopsRespond
}