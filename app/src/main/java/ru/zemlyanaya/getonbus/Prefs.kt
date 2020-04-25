package ru.zemlyanaya.getonbus

import com.kryptoprefs.context.KryptoContext
import com.kryptoprefs.preferences.KryptoPrefs

object PrefsConst {
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
    val isFirstLaunch = boolean(PrefsConst.PREF_FIRST_LAUNCH, true, true)
    val mode = string(PrefsConst.PREF_MODE, PrefsConst.PREF_MODE_FASTER, true)
    val isBusAllowed = boolean(PrefsConst.PREF_BUS, true, true)
    val isTramAllowed = boolean(PrefsConst.PREF_TRAM, true, true)
    val isTrolleyAllowed = boolean(PrefsConst.PREF_TROLLEY, true, true)
    val isMetroAllowed = boolean(PrefsConst.PREF_METRO, true, true)

    fun setPref(key: String, value: Any){
        when(key){
            PrefsConst.PREF_FIRST_LAUNCH -> isFirstLaunch.put(value as Boolean)
            PrefsConst.PREF_MODE -> mode.put(value as String)
            PrefsConst.PREF_BUS -> isBusAllowed.put(value as Boolean)
            PrefsConst.PREF_TRAM -> isTramAllowed.put(value as Boolean)
            PrefsConst.PREF_TROLLEY -> isTrolleyAllowed.put(value as Boolean)
            PrefsConst.PREF_METRO -> isMetroAllowed.put(value as Boolean)
            else -> throw Exception("Unknown pref!")
        }
    }
}