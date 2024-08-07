package com.domker.doctor.hardware

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.domker.doctor.hardware.databinding.FragmentHardwareBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

/**
 * 硬件相关的展示的总入口
 */
class HardwareFragment : Fragment() {

    private lateinit var binding: FragmentHardwareBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHardwareBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    /**
     * 初始化对应关系，打通NavView和其它的Fragment的关联
     */
    private fun initViews() {
        val navView: BottomNavigationView = binding.hardwareNavView
        // 一直显示图标和文字
        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_sensor,
            R.id.navigation_supervisor
        ))
        val controller = findNavController()
//        NavigationUI.setupActionBarWithNavController(activity as AppCompatActivity, controller, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.hardwareNavView, controller)
    }

    private fun findNavController(): NavController {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.hardware_nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }
}