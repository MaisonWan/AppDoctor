package com.domker.app.doctor.main.device

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.ActivityDeviceBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode

class DeviceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        val navView: BottomNavigationView = binding.deviceNavView
        // 一直显示图标和文字
        navView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.device_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_system, R.id.navigation_store))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}