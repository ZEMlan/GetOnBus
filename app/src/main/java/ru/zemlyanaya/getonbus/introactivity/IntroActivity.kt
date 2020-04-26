package ru.zemlyanaya.getonbus.introactivity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import ru.zemlyanaya.getonbus.App.Companion.prefs
import ru.zemlyanaya.getonbus.R
import ru.zemlyanaya.getonbus.mainactivity.MainActivity


class IntroActivity : FragmentActivity() {


    private lateinit var viewPager2 : ViewPager2
    private lateinit var adapter: ViewPagerFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            viewPager2.currentItem = if (viewPager2.currentItem == 0) 0 else viewPager2.currentItem-1
        }
        val butNext : MaterialButton = findViewById(R.id.butNext)
        butNext.setOnClickListener {
            viewPager2.currentItem = if (viewPager2.currentItem == 2) 2 else viewPager2.currentItem+1
        }
    }

    private fun setViewPager() {
        viewPager2 = findViewById(R.id.viewPager2)

        adapter = ViewPagerFragmentAdapter(this)
        adapter.addFragment(SlideFragment(R.layout.intro_slide1))
        adapter.addFragment(SlideFragment(R.layout.intro_slide2))
        adapter.addFragment(SlideFragment(R.layout.intro_slide3))

        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        viewPager2.adapter = adapter
    }

    fun setPref(key: String, value: Any){
        prefs.setPref(key, value)
    }

    fun goToNextActivity() = startActivity(Intent(this@IntroActivity, MainActivity::class.java))

}
