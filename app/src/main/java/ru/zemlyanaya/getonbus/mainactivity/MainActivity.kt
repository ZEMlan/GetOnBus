package ru.zemlyanaya.getonbus.mainactivity

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Shader.TileMode
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.head_bar.*
import ru.zemlyanaya.getonbus.IOnBackPressed
import ru.zemlyanaya.getonbus.R
import ru.zemlyanaya.getonbus.mainactivity.about.AboutFragment
import ru.zemlyanaya.getonbus.mainactivity.profile.ProfileFragment
import ru.zemlyanaya.getonbus.mainactivity.routing.RoutingFragment
import ru.zemlyanaya.getonbus.mainactivity.trip.TripFragment


class MainActivity : FragmentActivity(), RoutingFragment.OnGoInteractionListener {

    private var doubleBackToExitPressedOnce = false
    private val mHandler: Handler = Handler()
    private val mRunnable = Runnable { doubleBackToExitPressedOnce = false }

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
            intArrayOf(Color.rgb(8, 64, 215),
                Color.rgb(71, 120, 254),
                Color.rgb(105, 155, 254),
                Color.rgb(142, 173, 241)
                ),
            floatArrayOf(0.1f, 0.7f, 0.9f, 1f),
            TileMode.CLAMP
        )
        label.paint.shader = textShader
        label.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.frameMain)
            if(fragment is RoutingFragment)
                showAboutFragment()
            else
                showRouteFragment()
        }

        butProfile.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.frameMain)
            if(fragment is RoutingFragment)
                showProfileFragment()
            else
                showRouteFragment()
        }
        butMap.setOnClickListener { showMapFragment() }

        showRouteFragment()
    }

    override fun onGoInteraction(a: String, b: String) {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frameMain, TripFragment.newInstance(a, b))
            .commitAllowingStateLoss()
        header.visibility = View.GONE
    }

    private fun showMapFragment(){}

    private fun showProfileFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frameMain, ProfileFragment())
            .commitAllowingStateLoss()
        butProfile.setImageResource(R.drawable.ic_route)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frameMain)
        if(fragment is RoutingFragment)
            onRouteFragmentBackPressed()
        else {
            (fragment as? IOnBackPressed)?.onBackPressed()?.let {
            if(it)
                showRouteFragment()
            }
        }
    }

    private fun onRouteFragmentBackPressed(){
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Нажмите ещё раз для выхода", Toast.LENGTH_SHORT).show()

        mHandler.postDelayed(mRunnable, 2000)
    }

    private fun showAboutFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frameMain, AboutFragment())
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    private fun showRouteFragment(){
        header.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frameMain, RoutingFragment())
            .commitAllowingStateLoss()
        butProfile.setImageResource(R.drawable.ic_account)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacks(mRunnable)
    }

}
