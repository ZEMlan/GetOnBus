package ru.zemlyanaya.getonbus

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.zemlyanaya.getonbus.mainactivity.model.Stops

interface IOnBackPressed {
    fun onBackPressed(): Boolean
}

interface ServerApi{
    @GET("/getRoute")
    fun getRoute(
        @Query("From") from: Int,
        @Query("To") to: Int,
        @Query("Allowed transport") transport: List<String>,
        @Query("Routing") mode: String
    ): Call<List<String>>

    @POST("/getNames")
    fun getNames(@Query("Names") names: List<Int>): Call<String>

    @POST("/getStops")
    fun getAllStops(): Call<Stops>


}