package com.domker.app.doctor.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.app.doctor.AppAnalyzeActivity
import com.domker.app.doctor.R
import com.domker.app.doctor.entiy.AppItemInfo
import com.domker.app.doctor.util.DateUtil
import com.domker.app.doctor.util.IntentUtil
import com.domker.app.doctor.util.Router
import com.domker.app.doctor.widget.AppDetailAdapter
import com.domker.app.doctor.widget.AppDetailItemDiffCallBack
import com.domker.base.SystemVersion
import com.domker.base.addItemDecoration
import com.domker.base.file.FileUtils
import com.domker.base.toChinese
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment() {
    private lateinit var adapter: AppDetailAdapter
    private lateinit var homeViewModel: HomeViewModel
    private var appPackageName: String? = null
    private var apkSourcePath: String? = null

    private var detailList = mutableListOf<AppItemInfo>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = AppAnalyzeActivity.homeViewModel
                ?: ViewModelProvider(this).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_main, container, false)
        initButtonListener(root)
        initView(root)
        initObserver(root)
        return root
    }

    private fun initView(root: View) {
        val context = requireContext()

        // recyclerview
        val recyclerViewAppInfo: RecyclerView = root.findViewById(R.id.recyclerViewAppInfo)
        recyclerViewAppInfo.layoutManager = LinearLayoutManager(context)
        recyclerViewAppInfo.addItemDecoration(context, R.drawable.inset_recyclerview_divider)
        recyclerViewAppInfo.setItemViewCacheSize(100)
        adapter = AppDetailAdapter(context, AppDetailItemDiffCallBack())
        recyclerViewAppInfo.adapter = adapter
    }

    private fun initButtonListener(root: View) {
        val context = requireContext()
        root.findViewById<Button>(R.id.buttonStart).setOnClickListener {
            Snackbar.make(it, R.string.action_launch_app, Snackbar.LENGTH_LONG)
                    .setAction(R.string.launch) {
                        appPackageName?.apply {
                            val intent = IntentUtil.createLaunchIntent(context, this)
                            startActivity(intent)
                        }
                    }.show()
        }

        root.findViewById<Button>(R.id.buttonApkExplorer).setOnClickListener { _ ->
            apkSourcePath?.apply {
                ARouter.getInstance()
                        .build(Router.EXPLORER_ACTIVITY)
                        .withString("apk_source_path", this)
                        .withString("package_name", appPackageName)
                        .navigation()
            }
        }

        root.findViewById<Button>(R.id.buttonUninstall).setOnClickListener {
            Snackbar.make(it, R.string.action_uninstall_app, Snackbar.LENGTH_LONG)
                    .setAction(R.string.uninstall) {
                        appPackageName?.apply {
                            val uri: Uri = Uri.parse("package:$this")
                            val intent = Intent(Intent.ACTION_DELETE, uri)
                            startActivity(intent)
                        }
                    }.show()
        }
    }

    private fun initObserver(root: View) {
        val appName: TextView = root.findViewById(R.id.app_name)
        val appPackage: TextView = root.findViewById(R.id.app_package)
        val appIcon: ImageView = root.findViewById(R.id.app_icon)

        homeViewModel.appInfo.observe(viewLifecycleOwner, Observer {
            appPackageName = it.packageName
            apkSourcePath = it.sourceDir
            appName.text = "${it.appName} (${it.versionName})"
            appPackage.text = it.packageName
            appIcon.setImageDrawable(it.iconDrawable)

            detailList.add(AppItemInfo("版本名", it.versionName))
            detailList.add(AppItemInfo("版本号", it.versionCode.toString()))
            val t = SystemVersion.getVersion(it.targetSdkVersion)
            detailList.add(AppItemInfo("目标版本号", SystemVersion.getShowLabel(t)))
            val v = SystemVersion.getVersion(it.minSdkVersion)
            detailList.add(AppItemInfo("最低支持系统版本", SystemVersion.getShowLabel(v)))
            detailList.add(AppItemInfo("系统应用", it.isSystemApp.toChinese()))
            detailList.add(AppItemInfo("首次安装时间", DateUtil.getDataFromTimestamp(it.installTime)))
            detailList.add(AppItemInfo("最近更新时间", DateUtil.getDataFromTimestamp(it.updateTime)))
            detailList.add(AppItemInfo("Application名称", it.applicationName!!))
            detailList.add(AppItemInfo("源文件路径", it.sourceDir!!))
            detailList.add(AppItemInfo("源文件大小", FileUtils.formatFileSize(it.sourceApkSize!!)))
            detailList.add(AppItemInfo("Native库路径", it.nativeLibraryDir!!))
            detailList.add(AppItemInfo("主要CPU架构", it.primaryCpuAbi ?: ""))
            detailList.add(AppItemInfo("Data路径", it.dataDir!!))
            detailList.add(AppItemInfo("主进程名", it.processName!!))
            detailList.add(AppItemInfo("User ID", it.uid.toString()))
            detailList.add(AppItemInfo("Flag", it.flag ?: "Unknown"))

            adapter.setDetailList(detailList)
            adapter.notifyDataSetChanged()
        })
    }
}
