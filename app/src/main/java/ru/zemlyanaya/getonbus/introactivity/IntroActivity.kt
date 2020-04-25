package ru.zemlyanaya.getonbus.introactivity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.kryptoprefs.preferences.KryptoBuilder
import ru.zemlyanaya.getonbus.Keys
import ru.zemlyanaya.getonbus.Prefs
import ru.zemlyanaya.getonbus.R
import ru.zemlyanaya.getonbus.mainactivity.MainActivity


class IntroActivity : FragmentActivity() {
    private lateinit var prefs : Prefs

    private lateinit var viewPager2 : ViewPager2
    private lateinit var adapter: ViewPagerFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = Prefs(KryptoBuilder.hybrid(this, Keys.PREFS_NAME))
        val isFirstLaunch = prefs.isFirstLaunch.get()
        if(!isFirstLaunch)
            goToNextActivity()

        prefs.isFirstLaunch.put(false)

        setContentView(R.layout.activity_intro)

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        setViewPager()

        val butPrev : MaterialButton = findViewById(R.id.butPrev)
        butPrev.setOnClickListener {
            viewPager2.currentItem = if (viewPager2.currentItem == 1) 0 else 0
        }
        val butNext : MaterialButton = findViewById(R.id.butNext)
        butNext.setOnClickListener {
            viewPager2.currentItem = if (viewPager2.currentItem == 0) 1 else 1
        }
    }

    private fun setViewPager() {
        viewPager2 = findViewById(R.id.viewPager2)

        adapter = ViewPagerFragmentAdapter(this)
        adapter.addFragment(SlideFragment(R.layout.intro_slide1))
        adapter.addFragment(SlideFragment(R.layout.intro_slide2))

        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        viewPager2.adapter = adapter
    }

    fun setPrefFast(isFaster: Boolean){
        prefs.faster.put(isFaster)
    }

    fun goToNextActivity() = startActivity(Intent(this@IntroActivity, MainActivity::class.java))
}
