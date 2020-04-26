package ru.zemlyanaya.getonbus.mainactivity.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jem.fliptabs.FlipTab
import kotlinx.android.synthetic.main.fragment_profile.view.*
import ru.zemlyanaya.getonbus.App.Companion.prefs
import ru.zemlyanaya.getonbus.IOnBackPressed
import ru.zemlyanaya.getonbus.PrefsConst
import ru.zemlyanaya.getonbus.R

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(), IOnBackPressed {

    override fun onBackPressed(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_profile, container, false)


        val flipTab = layout.flipTab
        if(prefs.mode.get() == PrefsConst.PREF_MODE_FASTER)
            flipTab.selectLeftTab(false)
        else
            flipTab.selectRightTab(false)
        flipTab.setTabSelectedListener(object: FlipTab.TabSelectedListener {
            //left = faster, right = cheaper
            override fun onTabSelected(isLeftTab: Boolean, tabTextValue: String) {
                if(isLeftTab) prefs.mode.put(PrefsConst.PREF_MODE_FASTER)
                else prefs.mode.put(PrefsConst.PREF_MODE_CHEAPER)
            }
            override fun onTabReselected(isLeftTab: Boolean, tabTextValue: String) {
            }
        })

        val checkBus = layout.checkBus
        checkBus.isChecked = prefs.isBusAllowed.get()
        checkBus.setOnCheckedChangeListener { _, isChecked -> prefs.isBusAllowed.put(isChecked) }

        val checkTram = layout.checkTram
        checkTram.isChecked = prefs.isTramAllowed.get()
        checkTram.setOnCheckedChangeListener { _, isChecked -> prefs.isTramAllowed.put(isChecked) }

        val checkTrolley = layout.checkTrolley
        checkTrolley.isChecked = prefs.isTrolleyAllowed.get()
        checkTrolley.setOnCheckedChangeListener { _, isChecked -> prefs.isTrolleyAllowed.put(isChecked)}

        val checkMetro = layout.checkMetro
        checkMetro.isChecked = prefs.isMetroAllowed.get()
        checkMetro.setOnCheckedChangeListener { _, isChecked -> prefs.isMetroAllowed.put(isChecked)}

        return layout
    }

}
