package com.domker.doctor.app.detail.lib

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.domker.doctor.R
import com.domker.doctor.databinding.FragmentAppLibBinding
import com.domker.doctor.app.detail.AppDetailViewModel
import com.domker.base.file.ZipFileItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * 展示App中lib库的平台类型，以及每个类型下面包含的数量
 */
class AppLibFragment : Fragment() {
    private lateinit var binding: FragmentAppLibBinding
    private val detailViewModel: AppDetailViewModel by activityViewModels()

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
        detailViewModel.getApkDetail().observe(viewLifecycleOwner) { map ->
            // 按照固定顺序排序标题
            val titles = map.keys.sorted().toList()
            if (titles.isEmpty()) {
                initEmptyView(root, viewpager, tabLayout)
            } else {
                initViewPager(context, titles, map, viewpager, tabLayout)
            }
        }

    }

    private fun initEmptyView(
        root: View,
        viewpager: ViewPager2,
        tabLayout: TabLayout
    ) {
        viewpager.visibility = View.GONE
        tabLayout.visibility = View.GONE
        root.findViewById<View?>(R.id.empty_layout)?.visibility = View.VISIBLE
        root.findViewById<TextView>(R.id.textViewEmpty)?.text = getString(R.string.lib_empty_data)
    }

    private fun initViewPager(
        context: Context,
        titles: List<String>,
        map: Map<String, List<ZipFileItem>>,
        viewpager: ViewPager2,
        tabLayout: TabLayout
    ) {
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