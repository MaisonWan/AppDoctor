package com.domker.app.doctor

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.app.doctor.data.AppCheckFactory
import com.domker.app.doctor.databinding.ActivityMainBinding
import com.domker.app.doctor.util.Router
import com.domker.app.doctor.widget.AppDiffCallBack
import com.domker.app.doctor.widget.AppListAdapter
import com.domker.base.addItemDecoration
import com.domker.base.thread.AppExecutors

/**
 * 主界面，显示程序列表
 */
@Route(path = Router.MAIN_ACTIVITY)
class MainActivity : AppCompatActivity() {
    // 是否展示所有app，true包含系统应用
    private var isShowAllApp = false
    private lateinit var app: AppCheckFactory
    private lateinit var adapter: AppListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarTitle.title = getString(R.string.menu_custom_app)
        app = AppCheckFactory.getInstance(applicationContext)
        setSupportActionBar(binding.toolbarTitle)
        initViews()
        updateAppList(isShowAllApp)
    }

    private fun initViews() {
        binding.recyclerPackageList.layoutManager = LinearLayoutManager(this)
        binding.recyclerPackageList.addItemDecoration(this, R.drawable.inset_recyclerview_divider)
        adapter = AppListAdapter(this)
        binding.recyclerPackageList.adapter = adapter
        adapter.setOnItemClickListener { _, packageName ->
            ARouter.getInstance()
                    .build(Router.ANALYZE_ACTIVITY)
                    .withString("package_name", packageName)
                    .navigation()
        }
    }

    /**
     * 刷新app列表
     */
    private fun updateAppList(includeSystemApp: Boolean = false) {
        val newAppList = app.getAppList(includeSystemApp)
        val oldAppList = adapter.getAppList()
        val diffResult = DiffUtil.calculateDiff(AppDiffCallBack(oldAppList, newAppList))
        adapter.setAppList(newAppList)
        diffResult.dispatchUpdatesTo(adapter)

        // 更新一下最新信息到数据库
        AppExecutors.executor.execute {
            AppCheckFactory.getInstance(applicationContext).updateInfoToDatabase()
            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_dashboard -> {
                ARouter.getInstance()
                        .build(Router.DASHBOARD_ACTIVITY)
                        .withBoolean("show_all_app", isShowAllApp)
                        .navigation()
                true
            }
            R.id.menu_show_app -> {
                isShowAllApp = !isShowAllApp
                if (isShowAllApp) {
                    binding.toolbarTitle.title = getString(R.string.menu_custom_app)
                    item.setIcon(R.drawable.ic_baseline_border_clear_24)
                } else {
                    binding.toolbarTitle.title = getString(R.string.menu_all_app)
                    item.setIcon(R.drawable.ic_outline_border_all_24)
                }
                updateAppList(isShowAllApp)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
