package com.domker.app.doctor.hardware

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.domker.app.doctor.hardware.databinding.FragmentHardwareBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

/**
 * 硬件相关的展示的总入口
 */
class HardwareFragment : Fragment() {

    private lateinit var viewModel: SensorViewModel
    private lateinit var binding: FragmentHardwareBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHardwareBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[SensorViewModel::class.java]
    }

    /**
     * 初始化对应关系，打通NavView和其它的Fragment的关联
     */
    private fun initViews() {
        val navView: BottomNavigationView = binding.hardwareNavView
        // 一直显示图标和文字
        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.hardware_nav_host_fragment) as NavHostFragment
        navView.setupWithNavController(navHostFragment.findNavController())
    }

}