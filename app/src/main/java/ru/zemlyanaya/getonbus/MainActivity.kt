package ru.zemlyanaya.getonbus

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Shader.TileMode
import android.os.Bundle
import android.text.TextPaint
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.head_bar.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    private fun initUI(){
        val textShader: Shader = LinearGradient(
            0f,
            0f,
            label.text.length.toFloat(),
            label.textSize,
            intArrayOf(Color.rgb(60, 111, 250),
                Color.rgb(71, 120, 254),
                Color.rgb(105, 155, 254),
                Color.rgb(142, 173, 241)
                ),
            floatArrayOf(0.1f, 0.7f, 0.9f, 1f),
            TileMode.CLAMP
        )
        label.paint.shader = textShader
    }
}
