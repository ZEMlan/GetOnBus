package ru.zemlyanaya.getonbus

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory


class App : Application() {

    private lateinit var retrofit: Retrofit

    override fun onCreate() {
        super.onCreate()
        retrofit = Retrofit.Builder()
            .baseUrl("https://umorili.herokuapp.com")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
        serverApi =
            retrofit.create(ServerApi::class.java)
    }

    companion object {
        private lateinit var serverApi: ServerApi
        val api: ServerApi
            get() = serverApi
    }
}