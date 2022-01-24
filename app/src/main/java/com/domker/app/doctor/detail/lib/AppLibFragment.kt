package com.domker.app.doctor.detail.lib

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.FragmentAppLibBinding
import com.domker.app.doctor.detail.component.ComponentPageAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * 展示App中lib库的平台类型，以及每个类型下面包含的数量
 */
class AppLibFragment : Fragment() {
    private lateinit var binding: FragmentAppLibBinding

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
        val titles = listOf("ARM 32bit", "ARM 64bit")

        val adapter = AppLibPageAdapter(context, viewLifecycleOwner, titles)
        viewpager.adapter = adapter
        viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}