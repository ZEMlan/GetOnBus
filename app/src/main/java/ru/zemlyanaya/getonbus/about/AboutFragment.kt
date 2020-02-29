package ru.zemlyanaya.getonbus.about


import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_about.view.*
import kotlinx.android.synthetic.main.head_bar.*

import ru.zemlyanaya.getonbus.R

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout =  inflater.inflate(R.layout.fragment_about, container, false)
        val label = layout.aboutLabel

        val textShader: Shader = LinearGradient(
            0f,
            0f,
            label.text.length.toFloat(),
            label.textSize,
            intArrayOf(
                Color.rgb(60, 111, 250),
                Color.rgb(71, 120, 254),
                Color.rgb(105, 155, 254),
                Color.rgb(142, 173, 241)
            ),
            floatArrayOf(0.1f, 0.7f, 0.9f, 1f),
            Shader.TileMode.MIRROR
        )
        label.paint.shader = textShader

        return layout
    }


}
