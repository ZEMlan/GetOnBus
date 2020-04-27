package ru.zemlyanaya.getonbus

import android.app.Application
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.kryptoprefs.preferences.KryptoBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import ru.zemlyanaya.getonbus.mainactivity.model.PlaceholderApi


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        preferences = Prefs(KryptoBuilder.hybrid(this, PrefsConst.PREFS_NAME))

//        serverApi = Retrofit.Builder()
//            .client(OkHttpClient().newBuilder().build())
//            .baseUrl("https://unknown")
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
//            .addConverterFactory(JacksonConverterFactory.create())
//            .build()
//            .create(IServerApi::class.java)

        tempApi = Retrofit.Builder()
            .client(OkHttpClient().newBuilder().build())
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(PlaceholderApi::class.java)
    }


    companion object {
        private lateinit var preferences : Prefs
        val prefs: Prefs
            get() = preferences

        private lateinit var serverApi: IServerApi
        private lateinit var tempApi: PlaceholderApi
        val api: PlaceholderApi
            get() = tempApi
    }
}