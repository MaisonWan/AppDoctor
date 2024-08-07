package com.domker.doctor.main.applist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.domker.doctor.R
import com.domker.doctor.databinding.FragmentAppListBinding
import com.domker.doctor.widget.ViewBindingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

/**
 * 展示App列表的页面，多种不同风格的展示
 */
class AppListFragment : ViewBindingFragment<FragmentAppListBinding>() {

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAppListBinding {
        return FragmentAppListBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val navView: BottomNavigationView = binding.appListNavView
        // 一直显示图标和文字
        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.device_nav_host_fragment) as NavHostFragment
        navView.setupWithNavController(navHostFragment.findNavController())
    }


//    private fun initViews(binding: FragmentAppListBinding) {
//        mAdapter = AppPageAdapter(this)
//        mAdapter.includeSystemApp = appIncludeAll
//        binding.viewpager.also { v ->
//            v.adapter = mAdapter
//            v.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//            binding.circlePageIndicator.setViewPager(v)
//            // 滑动界面的时候，记录当前最新的位置
//            v.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
//                    // 异步存储当前的状态
//                    lifecycleScope.launch {
//                        AppSettings.setLaunchPageIndex(requireContext(), position)
//                    }
//                }
//
//            })
//        }
//    }

}