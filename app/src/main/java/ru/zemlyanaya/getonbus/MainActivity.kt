package ru.zemlyanaya.getonbus

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Shader.TileMode
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.head_bar.*
import ru.zemlyanaya.getonbus.about.AboutFragment
import ru.zemlyanaya.getonbus.routing.RoutingFragment


class MainActivity : FragmentActivity(), RoutingFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initHeader()
    }

    private fun initHeader(){
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
        label.setOnClickListener { showAboutFragment() }

        butProfile.setOnClickListener { showProfileFragment() }
        butMap.setOnClickListener { showMapFragment() }

        showRouteFragment()
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented")
    }

    private fun showMapFragment(){}

    private fun showProfileFragment(){}

    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.frame)
        if (f !is RoutingFragment) {
            supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.frame, RoutingFragment())
                .commitAllowingStateLoss()
            header.visibility = View.VISIBLE
        }
    }

    private fun showAboutFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frame, AboutFragment())
            .addToBackStack(null)
            .commitAllowingStateLoss()
        header.visibility = View.GONE

    }

    private fun showRouteFragment(){
        header.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, RoutingFragment())
            .commitAllowingStateLoss()
    }
}
