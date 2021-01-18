package com.domker.app.doctor

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.domker.app.doctor.data.AppChecker
import com.domker.app.doctor.data.AppEntity
import com.domker.app.doctor.data.AppInfoViewModel
import com.domker.app.doctor.databinding.ActivityAppDetailBinding
import com.domker.app.doctor.databinding.ContentAppDetailBinding
import com.domker.app.doctor.entiy.AppItemInfo
import com.domker.app.doctor.util.DateUtil
import com.domker.app.doctor.util.IntentUtil
import com.domker.app.doctor.util.Router
import com.domker.app.doctor.widget.AppBarStateChangeListener
import com.domker.app.doctor.widget.AppDetailAdapter
import com.domker.app.doctor.widget.AppDetailItemDiffCallBack
import com.domker.base.thread.AppExecutors
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar


/**
 * 应用详细内容
 */
@Route(path = Router.DETAIL_ACTIVITY)
class AppDetailActivity : AppCompatActivity() {
    private val appChecker: AppChecker = AppChecker(this)
    private var detailList = mutableListOf<AppItemInfo>()
    private lateinit var appPackageName: String
    private lateinit var adapter: AppDetailAdapter
    private lateinit var appInfoViewModel: AppInfoViewModel
    private lateinit var binding: ActivityAppDetailBinding
    private lateinit var bindingContent: ContentAppDetailBinding
//    private val diffResult = DiffUtil.calculateDiff(AppItemDiffCallback(detailList, newData), true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindingContent = ContentAppDetailBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)

        initViewModel()
        updateAppInfo()
        initViews()
    }

    private fun initViewModel() {
//        appInfoViewModel = ViewModelProviders.of(this).get(AppInfoViewModel::class.java)
//        appInfoViewModel.getAppInfo().observe(this, Observer {
//            updateAppInfo(it)
//        })
//        appInfoViewModel.getActivityInfo().observe(this, Observer {
//            log("observe ${it.size}")
//            updateActivityInfo(it)
//        })
//        appInfoViewModel.getServiceInfo().observe(this, Observer {
//            updateServiceInfo(it)
//        })

        bindingContent.recyclerViewAppInfo.layoutManager = LinearLayoutManager(this)
        bindingContent.recyclerViewAppInfo.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//        recyclerViewAppInfo.setItemViewCacheSize(100)
        adapter = AppDetailAdapter(this, AppDetailItemDiffCallBack())
        bindingContent.recyclerViewAppInfo.adapter = adapter
    }

    /**
     * 更新App的信息
     */
    private fun updateAppInfo(info: AppEntity?) {
        if (info != null) {
            detailList.add(AppItemInfo("Package Name", info.packageName))
            detailList.add(AppItemInfo("System Application", info.isSystemApp.toString()))
            detailList.add(AppItemInfo("First Install Time", DateUtil.getDataFromTimestamp(info.installTime)))
            detailList.add(AppItemInfo("Last Update Time", DateUtil.getDataFromTimestamp(info.updateTime)))
            detailList.add(AppItemInfo("Version Name", info.versionName))
            detailList.add(AppItemInfo("Version Code", info.versionCode.toString()))
            detailList.add(AppItemInfo("Application Name", info.applicationName!!))
            detailList.add(AppItemInfo("Source Direction", info.sourceDir!!))
            detailList.add(AppItemInfo("Native Library Direction", info.nativeLibraryDir!!))
            detailList.add(AppItemInfo("Process Name", info.processName!!))

            // 展示的图标和app的名字
            binding.appIcon.setImageDrawable(info.iconDrawable)
            title = info.appName
        }

//        recyclerViewAppInfo.layoutManager = LinearLayoutManager(this)
//        recyclerViewAppInfo.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//        recyclerViewAppInfo.setItemViewCacheSize(100)
//        adapter = AppDetailAdapter(this, AppDetailItemDiffCallBack())
        adapter.setDetailList(detailList)
        adapter.notifyDataSetChanged()
//        recyclerViewAppInfo.adapter = adapter
//        adapter.submitList(detailList)

//        adapter.setOnItemClickListener { _, position ->
//            //            val activity = adapter.getItemList()?.get(position).toString()
//
//        }
    }

//    private fun updateActivityInfo(activityList: MutableList<ActivityInfo>) {
//        // activity list
//        detailList.add(AppItemInfo(TYPE_SUBJECT, null, "Activity"))
//        activityList.forEach {
//            detailList.add(AppItemInfo(TYPE_LABEL, null, it.name))
//        }
//        adapter.notifyDataSetChanged()
////        val diffResult = DiffUtil.calculateDiff(AppItemDiffCallback(detailList, newData), true)
////        detailList = newData
//
////        adapter.submitList(activityList)
//    }
//
//    private fun updateServiceInfo(serviceList: MutableList<ActivityInfo>) {
//        // service list
//
//        detailList.add(AppItemInfo(TYPE_SUBJECT, null, "Service"))
//        serviceList.forEach {
//            detailList.add(AppItemInfo(TYPE_LABEL, null, it.name))
//        }
//        adapter.setDetailList(detailList)
////        diffResult.dispatchUpdatesTo(adapter)
//        adapter.notifyDataSetChanged()
//    }

    private fun updateAppInfo() {
        appPackageName = intent.getStringExtra(IntentUtil.INTENT_KEY_PACKAGE)!!
        AppExecutors.executor.execute {
            appInfoViewModel.getAppInfo().postValue(appChecker.getAppEntity(appPackageName))
//            appInfoViewModel.getActivityInfo().postValue(appChecker.getActivityInfo(appPackageName))
//            appInfoViewModel.getServiceInfo().postValue(appChecker.getServiceInfo(appPackageName))
        }
    }


    private fun initViews() {
        binding.appIcon.setOnClickListener { view ->
            Snackbar.make(view, R.string.action_launch_app, Snackbar.LENGTH_LONG)
                    .setAction(R.string.launch) {
                        val intent = IntentUtil.createLaunchIntent(this, appPackageName)
                        startActivity(intent)
                    }.show()
        }

        binding.appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                when (state) {
                    State.COLLAPSED -> //折叠状态
                        binding.appIcon.visibility = View.INVISIBLE
                    else ->
                        binding.appIcon.visibility = View.VISIBLE
                }
            }
        })
    }
}
