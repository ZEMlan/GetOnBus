package ru.zemlyanaya.getonbus

import com.kryptoprefs.context.KryptoContext
import com.kryptoprefs.preferences.KryptoPrefs

object Keys {
    const val PREFS_NAME = "GetOnBusPrefs"
    const val PREF_FIRST_LAUNCH = "is_first_launch"
    const val PREF_MODE = "faster_or_cheaper"
    const val PREF_MODE_FASTER = "Faster"
    const val PREF_MODE_CHEAPER = "Cheaper"
    const val PREF_BUS = "bus"
    const val PREF_TRAM = "tram"
    const val PREF_TROLLEY = "trolley"
    const val PREF_METRO = "metro"
}

class Prefs(prefs: KryptoPrefs): KryptoContext(prefs) {
    val isFirstLaunch = boolean(Keys.PREF_FIRST_LAUNCH, true, true)
    val mode = string(Keys.PREF_MODE, Keys.PREF_MODE_FASTER, true)
    val isBusAllowed = boolean(Keys.PREF_BUS, true, true)
    val isTramAllowed = boolean(Keys.PREF_TRAM, true, true)
    val isTrolleyAllowed = boolean(Keys.PREF_TROLLEY, true, true)
    val isMetroAllowed = boolean(Keys.PREF_METRO, true, true)

    fun setPref(key: String, value: Any){
        when(key){
            Keys.PREF_FIRST_LAUNCH -> isFirstLaunch.put(value as Boolean)
            Keys.PREF_MODE -> mode.put(value as String)
            Keys.PREF_BUS -> isBusAllowed.put(value as Boolean)
            Keys.PREF_TRAM -> isTramAllowed.put(value as Boolean)
            Keys.PREF_TROLLEY -> isTrolleyAllowed.put(value as Boolean)
            Keys.PREF_METRO -> isMetroAllowed.put(value as Boolean)
            else -> throw Exception("Unknown pref!")
        }
    }
}