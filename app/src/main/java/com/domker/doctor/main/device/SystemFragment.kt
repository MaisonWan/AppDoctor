package com.domker.doctor.main.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.domker.doctor.R
import com.domker.doctor.databinding.FragmentDeviceSystemBinding
import com.google.android.material.tabs.TabLayoutMediator

private val TAB_TITLE =
    arrayOf(R.string.system_about, R.string.system_state, R.string.system_parameter)

/**
 * 系统信息
 */
class SystemFragment : Fragment() {
    private lateinit var binding: FragmentDeviceSystemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeviceSystemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 这里需要使用childFragmentManager，因为Fragment层叠使用，其它的FragmentManager会有冲突问题
        binding.viewpager.adapter = SystemPagerAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, i ->
            tab.setText(TAB_TITLE[i])
        }.attach()
    }
}