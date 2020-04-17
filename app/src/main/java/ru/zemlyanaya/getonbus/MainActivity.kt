package ru.zemlyanaya.getonbus

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Shader.TileMode
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.head_bar.*
import kotlinx.coroutines.*
import ru.zemlyanaya.getonbus.about.AboutFragment
import ru.zemlyanaya.getonbus.routing.RoutingFragment
import ru.zemlyanaya.getonbus.trip.TripFragment
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


class MainActivity : FragmentActivity(), RoutingFragment.OnGoInteractionListener {

    private var activityJob = Job()
    private val activityScope = CoroutineScope(Dispatchers.Main + activityJob)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initHeader()
        activityScope.launch(Dispatchers.Main) {
            val hasInternet = withContext(Dispatchers.IO) { hasInternetConnection() }
            Snackbar.make(
                frame.rootView,
                "Есть интернет-подключение:$hasInternet",
                Snackbar.LENGTH_LONG
            ).show()
        }
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

    override fun onGoInteraction(a: String, b: String) {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frame, TripFragment.newInstance(a, b))
            .commitAllowingStateLoss()
        header.visibility = View.GONE
    }

    private fun showMapFragment(){}

    private fun showProfileFragment(){}

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frame)
        (fragment as? IOnBackPressed)?.onBackPressed()?.let {
            if(it) {
                this.supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .replace(R.id.frame, RoutingFragment.newInstance())
                    .commitAllowingStateLoss()
                header.visibility = View.VISIBLE
            }
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
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frame, RoutingFragment.newInstance())
            .commitAllowingStateLoss()
    }

    private fun hasInternetConnection(): Boolean {
        return try {
            // Connect to Google DNS to check for connection
            val timeoutMs = 1500
            val socket = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)

            socket.connect(socketAddress, timeoutMs)
            socket.close()

            true
        } catch (e: IOException) {
            false
        }
    }

    fun cancelJob() {
        activityJob.cancel()
    }

}
