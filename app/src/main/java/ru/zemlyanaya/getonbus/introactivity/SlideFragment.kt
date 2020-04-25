package ru.zemlyanaya.getonbus.introactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.intro_slide2.view.*
import ru.zemlyanaya.getonbus.R

class SlideFragment(private val layoutID : Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lateinit var layout : View
        when (layoutID) {
            R.layout.intro_slide1 -> layout = inflater.inflate(R.layout.intro_slide1, container, false)
            else -> {
                layout = inflater.inflate(R.layout.intro_slide2, container, false)
                val butCheap = layout.butCheap
                butCheap.setOnClickListener {
                    (activity as IntroActivity).setPrefFast(false)
                    (activity as IntroActivity).goToNextActivity()
                }

                val butFast = layout.butFast
                butFast.setOnClickListener {
                    (activity as IntroActivity).setPrefFast(true)
                    (activity as IntroActivity).goToNextActivity()
                }
            }
        }
        return layout
    }

}
