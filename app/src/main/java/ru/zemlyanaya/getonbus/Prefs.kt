package ru.zemlyanaya.getonbus

import com.kryptoprefs.context.KryptoContext
import com.kryptoprefs.preferences.KryptoPrefs

object Keys {
    const val PREFS_NAME = "GetOnBusPrefs"
    const val PREF_FIRST_LAUNCH = "is_first_launch"
    const val PREF_FASTER = "is_faster"
}

class Prefs(prefs: KryptoPrefs): KryptoContext(prefs) {
//    val stringPref = string("stringPref", "defaultValue", true)
//    val intPref = int("intPref", 42, true)
    val isFirstLaunch = boolean(Keys.PREF_FIRST_LAUNCH, true, true)
    val faster = boolean(Keys.PREF_FASTER, true, true)
}