package com.domker.app.doctor.main.device

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.ActivityDeviceBinding
import com.domker.app.doctor.widget.BaseAppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode

/**
 * 设备信息管理，查看系统的属性已经存储等
 */
class DeviceActivity : BaseAppCompatActivity() {
    private lateinit var binding: ActivityDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initViews()
    }

    private fun initViews() {
        val navView: BottomNavigationView = binding.deviceNavView
        // 一直显示图标和文字
        navView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.device_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_system, R.id.navigation_store, R.id.navigation_properties))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}