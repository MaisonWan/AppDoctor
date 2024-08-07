package com.domker.doctor

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.alibaba.android.arouter.facade.annotation.Route
import com.domker.doctor.databinding.ActivityMainDrawerBinding
import com.domker.doctor.main.AppViewModel
import com.domker.doctor.settings.SettingsViewModel
import com.domker.doctor.store.AppSettings
import com.domker.doctor.util.Router
import kotlinx.coroutines.launch

@Route(path = Router.MAIN_ACTIVITY)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainDrawerBinding
    private lateinit var appViewModel: AppViewModel
    private lateinit var settingsViewModel: SettingsViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appViewModel = ViewModelProvider(this)[AppViewModel::class.java]
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        initToolbar()
        initHeaderView()
        initNavigation()
    }

    private fun initNavigation() {
        val navController = getNavController()

        val ids = setOf(
            R.id.nav_overview,
            R.id.nav_app_list,
            R.id.nav_photo_info,
            R.id.nav_battery,
            R.id.nav_hardware,
            R.id.nav_system_info,
            R.id.nav_dashboard
        )
        appBarConfiguration = AppBarConfiguration(ids, binding.drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    private fun initToolbar() {
        binding.appBar.toolbar.also {
            setSupportActionBar(it)
        }.setNavigationOnClickListener {
            // 在配置界面点击返回，直接执行返回操作
            if (getNavController().currentDestination?.id == R.id.nav_settings) {
                onBackPressed()
            } else {
                // 其它界面打开左侧边栏
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun initHeaderView() {
        val headerView = binding.navView.getHeaderView(0)
        val switchAppInclude = headerView.findViewById<SwitchCompat>(R.id.switchAppInclude)

        // 异步加载配置
        appViewModel.loadLaunchSettings(this@MainActivity)

        appViewModel.observeLoadSettingsOnce(this) {
            // 第一次初始化，切换按钮展示
            switchAppInclude.isChecked = it.includeAllApp
            it.appListStyle
        }

        switchAppInclude.setOnCheckedChangeListener { buttonView, isChecked ->
            with(buttonView) {
                if (isChecked) {
                    setText(R.string.nav_header_switch_all)
                } else {
                    setText(R.string.nav_header_switch_install)
                }
            }

            lifecycleScope.launch {
                AppSettings.setIncludeAllApp(this@MainActivity, isChecked)
            }
            appViewModel.switchChanged(isChecked)
        }

        // 打开配置
        val settingButton = headerView.findViewById<ImageView>(R.id.imageViewSettings)
        settingButton.setOnClickListener {
            val navController = getNavController()
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            navController.navigate(R.id.nav_settings)
        }
    }

    private fun getNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}