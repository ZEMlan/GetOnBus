package ru.zemlyanaya.getonbus

import android.app.Application
import com.kryptoprefs.preferences.KryptoBuilder


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        preferences = Prefs(KryptoBuilder.hybrid(this, PrefsConst.PREFS_NAME))
    }

    companion object {
        private lateinit var preferences : Prefs
        val prefs: Prefs
            get() = preferences

    }
}