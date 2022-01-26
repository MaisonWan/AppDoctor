package com.domker.app.doctor.detail

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.alibaba.android.arouter.facade.annotation.Route
import com.domker.app.doctor.R
import com.domker.app.doctor.data.AppChecker
import com.domker.app.doctor.detail.component.ComponentViewModel
import com.domker.app.doctor.detail.home.HomeViewModel
import com.domker.app.doctor.util.IntentUtil
import com.domker.app.doctor.util.Router
import com.domker.app.doctor.widget.BaseAppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

@Route(path = Router.DETAIL_ACTIVITY)
class AppDetailActivity : BaseAppCompatActivity() {
    private lateinit var appChecker: AppChecker
    private lateinit var appPackageName: String

    // 组件的ViewModel
    private lateinit var componentViewModel: ComponentViewModel
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_detail)
        initToolbar()

        appChecker = AppChecker(this)
        appPackageName = intent.getStringExtra(IntentUtil.INTENT_KEY_PACKAGE)!!

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        componentViewModel = ViewModelProvider(this)[ComponentViewModel::class.java]

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        // 一直显示图标和文字
        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED

        val navController = findNavController()
//        val graph = navController.graph
//        graph.addArgument("homeViewModel", NavArgument.Builder().setDefaultValue(homeViewModel).build())

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_main, R.id.navigation_permission,
                R.id.navigation_component, R.id.navigation_lib, R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        updateAppInfo()
    }

    private fun findNavController(): NavController {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }

    /**
     * 提前初始化数据，这样Fragment展示的时候，数据已经Ready
     */
    private fun updateAppInfo() {
        homeViewModel.updateData(appChecker, appPackageName)
        componentViewModel.updateData(appChecker, appPackageName)
    }

}
