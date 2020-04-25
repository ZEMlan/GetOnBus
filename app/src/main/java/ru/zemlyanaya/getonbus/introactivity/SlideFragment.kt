package ru.zemlyanaya.getonbus.introactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.intro_slide2.view.*
import kotlinx.android.synthetic.main.intro_slide3.view.*
import ru.zemlyanaya.getonbus.Keys
import ru.zemlyanaya.getonbus.R

class SlideFragment(private val layoutID : Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lateinit var layout : View
        when (layoutID) {
            R.layout.intro_slide1 -> layout = inflater.inflate(R.layout.intro_slide1, container, false)

            R.layout.intro_slide2 -> {
                layout = inflater.inflate(R.layout.intro_slide2, container, false)
                val checkBus = layout.checkBus
                checkBus.setOnCheckedChangeListener { _, isChecked ->
                    (activity as IntroActivity).setPref(Keys.PREF_BUS, isChecked)
                }

                val checkTram = layout.checkTram
                checkTram.setOnCheckedChangeListener { _, isChecked ->
                    (activity as IntroActivity).setPref(Keys.PREF_TRAM, isChecked)
                }

                val checkTrolley = layout.checkTrolley
                checkTrolley.setOnCheckedChangeListener { _, isChecked ->
                    (activity as IntroActivity).setPref(Keys.PREF_TROLLEY, isChecked)
                }

                val checkMetro = layout.checkMetro
                checkMetro.setOnCheckedChangeListener { _, isChecked ->
                    (activity as IntroActivity).setPref(Keys.PREF_METRO, isChecked)
                }
            }

            else -> {
                layout = inflater.inflate(R.layout.intro_slide3, container, false)
                val butCheap = layout.butCheap
                butCheap.setOnClickListener {
                    (activity as IntroActivity).setPref(Keys.PREF_MODE, Keys.PREF_MODE_CHEAPER)
                    (activity as IntroActivity).goToNextActivity()
                }

                val butFast = layout.butFast
                butFast.setOnClickListener {
                    (activity as IntroActivity).setPref(Keys.PREF_MODE, Keys.PREF_MODE_FASTER)
                    (activity as IntroActivity).goToNextActivity()
                }
            }
        }
        return layout
    }

}
