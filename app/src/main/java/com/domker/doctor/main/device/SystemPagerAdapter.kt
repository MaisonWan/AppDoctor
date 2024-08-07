package com.domker.doctor.main.device

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * 系统状态分页适配器
 * Created by wanlipeng on 2/19/21 8:52 PM
 */
class SystemPagerAdapter(private val fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SystemAboutFragment.newInstance()
            1 -> SystemStateFragment.newInstance()
            else -> SystemParametersFragment.newInstance()
        }
    }
}