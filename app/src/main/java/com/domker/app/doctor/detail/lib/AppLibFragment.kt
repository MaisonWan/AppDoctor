package com.domker.app.doctor.detail.lib

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.FragmentAppLibBinding
import com.domker.app.doctor.detail.home.HomeViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * 展示App中lib库的平台类型，以及每个类型下面包含的数量
 */
class AppLibFragment : Fragment() {
    private lateinit var binding: FragmentAppLibBinding
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppLibBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }

    private fun initViews(root: View) {
        val context = requireContext()
        val tabLayout: TabLayout = root.findViewById(R.id.tabLayout)
        val viewpager: ViewPager2 = root.findViewById(R.id.viewpager)

        // 监听异步获取的结果
        homeViewModel.getApkDetail().observe(viewLifecycleOwner) { map ->
            val titles = map.keys.sorted().toList()
            val adapter = AppLibPageAdapter(context, titles, map)
            viewpager.adapter = adapter
            viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            if (titles.size > 3) {
                tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
            } else {
                tabLayout.tabMode = TabLayout.MODE_FIXED
            }
            TabLayoutMediator(tabLayout, viewpager) { tab, position ->
                tab.text = titles[position].uppercase()
            }.attach()
        }

    }
}