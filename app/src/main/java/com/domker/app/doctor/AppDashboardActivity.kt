package com.domker.app.doctor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.domker.app.doctor.data.AppCheckFactory
import com.domker.app.doctor.data.AppEntity
import com.domker.app.doctor.ui.main.DashboardPagerAdapter
import com.domker.app.doctor.util.Router
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@Route(path = Router.DASHBOARD_ACTIVITY)
class AppDashboardActivity : AppCompatActivity() {
    private var appList: List<AppEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_dashboard)

        appList = AppCheckFactory.getInstance(applicationContext).getAppList()
        initViews()
    }

    private fun initViews() {
        val tabTitle = intArrayOf(R.string.item_package_size, R.string.item_install_time)
        val sectionsPagerAdapter = DashboardPagerAdapter(this, tabTitle, appList!!)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.setText(tabTitle[position])
        }).attach()

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

}