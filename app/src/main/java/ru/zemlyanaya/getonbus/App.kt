package ru.zemlyanaya.getonbus

import android.app.Application
import com.kryptoprefs.preferences.KryptoBuilder
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory


class App : Application() {

    private lateinit var retrofit: Retrofit

    override fun onCreate() {
        super.onCreate()

        preferences = Prefs(KryptoBuilder.hybrid(this, PrefsConst.PREFS_NAME))

        retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

        serverApi = retrofit.create(IServerApi::class.java) }


    companion object {
        private lateinit var preferences : Prefs
        val prefs: Prefs
            get() = preferences

        private lateinit var serverApi: IServerApi
        val api: IServerApi
            get() = serverApi
    }
}