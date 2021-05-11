package com.domker.app.doctor.main.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.FragmentMainDeviceBinding
import com.domker.app.doctor.widget.BaseAppFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode

/**
 * 本机信息
 * Created by wanlipeng on 2021/5/11 4:54 下午
 */
class DeviceFragment : BaseAppFragment() {
    private lateinit var binding: FragmentMainDeviceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainDeviceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val navView: BottomNavigationView = binding.deviceNavView
        // 一直显示图标和文字
        navView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.device_nav_host_fragment) as NavHostFragment
        navView.setupWithNavController(navHostFragment.findNavController())
    }
}