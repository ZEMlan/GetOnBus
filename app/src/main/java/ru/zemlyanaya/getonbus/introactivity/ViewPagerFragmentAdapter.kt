package ru.zemlyanaya.getonbus.introactivity

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerFragmentAdapter(@NonNull fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val fragments: ArrayList<Fragment> = ArrayList()

    fun addFragment(fragment: Fragment) = fragments.add(fragment)

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}