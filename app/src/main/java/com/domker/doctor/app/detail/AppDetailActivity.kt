package com.domker.doctor.app.detail

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.alibaba.android.arouter.facade.annotation.Route
import com.domker.doctor.R
import com.domker.doctor.app.detail.component.ComponentViewModel
import com.domker.doctor.data.AppChecker
import com.domker.doctor.util.IntentUtil
import com.domker.doctor.util.Router
import com.domker.doctor.widget.BaseAppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

@Route(path = Router.DETAIL_ACTIVITY)
class AppDetailActivity : BaseAppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var appChecker: AppChecker
    private lateinit var appPackageName: String

    // 组件的ViewModel
    private lateinit var componentViewModel: ComponentViewModel
    private lateinit var detailViewModel: AppDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_detail)
        initToolbar()

        appChecker = AppChecker(this)
        appPackageName = intent.getStringExtra(IntentUtil.INTENT_KEY_PACKAGE)!!

        detailViewModel = ViewModelProvider(this)[AppDetailViewModel::class.java]
        componentViewModel = ViewModelProvider(this)[ComponentViewModel::class.java]

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        // 一直显示图标和文字
        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED

        val navController = findNavController()
//        val graph = navController.graph
//        graph.addArgument("homeViewModel", NavArgument.Builder().setDefaultValue(homeViewModel).build())

        // 顶级的页面有多个，所以在配置跳转中，单独配置5个顶级页面。不出现返回按钮。如果想完全和Nav里面设置的一样，使用navController.graph初始化
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_main, R.id.navigation_permission,
                R.id.navigation_component, R.id.navigation_lib, R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        updateAppInfo()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController()
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun findNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }

    /**
     * 提前初始化数据，这样Fragment展示的时候，数据已经Ready
     */
    private fun updateAppInfo() {
        detailViewModel.updateData(appChecker, appPackageName)
        componentViewModel.updateData(appChecker, appPackageName)
    }

}
