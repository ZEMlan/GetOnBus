package ru.zemlyanaya.getonbus

import android.app.Application
import com.kryptoprefs.preferences.KryptoBuilder
import retrofit2.Retrofit
import ru.zemlyanaya.getonbus.mainactivity.model.Stops


class App : Application() {

    private lateinit var retrofit: Retrofit

    override fun onCreate() {
        super.onCreate()

        preferences = Prefs(KryptoBuilder.hybrid(this, PrefsConst.PREFS_NAME))

//        retrofit = Retrofit.Builder()
//            .baseUrl("") //TODO add base uri
//            .addConverterFactory(JacksonConverterFactory.create())
//            .build()
//
//        serverApi = retrofit.create(ServerApi::class.java)

        getStops()
    }

    private fun getStops(){
        try {
            stopsObj = serverApi.getAllStops().execute().body()
        }
        catch (e: Exception){

        }
    }

    companion object {
        private lateinit var preferences : Prefs
        val prefs: Prefs
            get() = preferences

        private lateinit var serverApi: ServerApi
        val api: ServerApi
            get() = serverApi

        private lateinit var stopsObj: Stops
        val stops: Stops?
            get() = stopsObj
    }
}