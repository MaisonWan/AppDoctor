package com.domker.app.doctor.main.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.FragmentDeviceSystemBinding
import com.domker.app.doctor.util.DeviceScanner
import com.google.android.material.tabs.TabLayoutMediator

private val TAB_TITLE = arrayOf(R.string.system_about, R.string.system_state, R.string.system_parameter)

/**
 * 系统信息
 */
class SystemFragment : Fragment() {
    private lateinit var binding: FragmentDeviceSystemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentDeviceSystemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val data = DeviceScanner(requireContext()).getSystemProperties()
//        val adapter = PropertiesAdapter(requireContext(), data)
        binding.viewpager.adapter = SystemPagerAdapter(parentFragmentManager, lifecycle)
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, i ->
            tab.setText(TAB_TITLE[i])
        }.attach()
    }
}