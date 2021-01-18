package com.domker.app.doctor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.alibaba.android.arouter.facade.annotation.Route
import com.domker.app.doctor.data.AppChecker
import com.domker.app.doctor.ui.component.ComponentViewModel
import com.domker.app.doctor.ui.home.HomeViewModel
import com.domker.base.thread.AppExecutors
import com.domker.app.doctor.util.IntentUtil
import com.domker.app.doctor.util.Router
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode

@Route(path = Router.ANALYZE_ACTIVITY)
class AppAnalyzeActivity : AppCompatActivity() {
    private lateinit var appChecker: AppChecker
    private lateinit var appPackageName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_analyze)

        appChecker = AppChecker(this)
        appPackageName = intent.getStringExtra(IntentUtil.INTENT_KEY_PACKAGE)!!

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        componentViewModel = ViewModelProvider(this).get(ComponentViewModel::class.java)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        // 一直显示图标和文字
        navView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED

        val navController = findNavController(R.id.nav_host_fragment)
        val graph = navController.graph

//        graph.addArgument("homeViewModel", NavArgument.Builder().setDefaultValue(homeViewModel).build())

        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_main, R.id.navigation_permission, R.id.navigation_component, R.id.navigation_dashboard))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        updateAppInfo()
    }

    private fun updateAppInfo() {
        AppExecutors.executor.execute {
            appChecker.getAppEntity(appPackageName)?.let {
                homeViewModel?.appInfo?.postValue(it)
            }
            componentViewModel?.activityInfo?.postValue(appChecker.getActivityListInfo(appPackageName))
            componentViewModel?.serviceInfo?.postValue(appChecker.getServiceListInfo(appPackageName))
            componentViewModel?.providerInfo?.postValue(appChecker.getProvidersListInfo(appPackageName))
            componentViewModel?.receiverInfo?.postValue(appChecker.getReceiversListInfo(appPackageName))
            componentViewModel?.permissionInfo?.postValue(appChecker.getPermissions(appPackageName))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeViewModel = null
        componentViewModel = null
    }

    companion object {
        var homeViewModel: HomeViewModel? = null
        var componentViewModel: ComponentViewModel? = null
    }
}
