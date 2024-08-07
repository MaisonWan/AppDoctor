package com.domker.doctor.main.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.domker.doctor.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class DeviceInfoFragment : Fragment() {

    private lateinit var galleryViewModel: SystemInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel = ViewModelProvider(this).get(SystemInfoViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_main_device, container, false)
        init(root)
        return root
    }

    private fun init(root: View) {
        val navView: BottomNavigationView = root.findViewById(R.id.device_nav_view)
        // 一直显示图标和文字
        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        val navHost: FragmentContainerView = root.findViewById(R.id.device_nav_host_fragment)
//        val navHostFragment = findFragmentById(R.id.device_nav_host_fragment) as NavHostFragment
        val navController = navHost.findNavController()

//        val navController = Navigation.findNavController(root)
//        val navController = findLocalNavController()
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.navigation_system, R.id.navigation_store))
        NavigationUI.setupActionBarWithNavController(
            requireActivity() as AppCompatActivity,
            navController,
            appBarConfiguration
        )
        navView.setupWithNavController(navController)
    }

    private fun findLocalNavController(): NavController {
//        parentFragmentManager.primaryNavigationFragment
//        Navigation.findNavController()
        return Navigation.findNavController(requireActivity(), R.id.device_nav_host_fragment)
//        val host = this.parentFragmentManager.findFragmentById(R.id.device_nav_host_fragment) as NavHostFragment
//        return host.navController
    }
}