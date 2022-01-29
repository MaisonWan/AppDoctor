package com.domker.app.doctor.detail.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.domker.app.doctor.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * 展示app中的四大组件
 * Created by wanlipeng on 2020/6/7 3:17 PM
 */
class ComponentFragment : Fragment() {
    private lateinit var adapter: ComponentPageAdapter
    private val componentViewModel: ComponentViewModel by activityViewModels()

    // Tab标题的资源
    private val tabTitleRes =
        intArrayOf(R.string.item_activity, R.string.item_service, R.string.item_provider, R.string.item_receiver, R.string.item_meta_data)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 获取ViewModel
        initView(view)
    }

    private fun initView(root: View) {
        val context = requireContext()
        val tabLayout: TabLayout = root.findViewById(R.id.tabLayout)
        val viewpager: ViewPager2 = root.findViewById(R.id.viewpager)
        adapter = ComponentPageAdapter(context, viewLifecycleOwner, componentViewModel, tabTitleRes)
        viewpager.adapter = adapter
        viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//        viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
//            override fun onPageScrollStateChanged(state: Int) {
//                super.onPageScrollStateChanged(state)
//            }
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//            }
//
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//            }
//        })
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            tab.setText(tabTitleRes[position])
        }.attach()

    }

}