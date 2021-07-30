package com.domker.app.doctor.detail.home

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.app.doctor.R
import com.domker.app.doctor.detail.AppDetailActivity
import com.domker.app.doctor.entiy.AppItemInfo
import com.domker.app.doctor.entiy.AppItemInfo.Companion.TYPE_PACKAGE
import com.domker.app.doctor.util.DataFormat
import com.domker.app.doctor.util.IntentUtil
import com.domker.app.doctor.util.Router
import com.domker.app.doctor.widget.AppDetailAdapter
import com.domker.app.doctor.widget.AppDetailItemDiffCallBack
import com.domker.base.SystemVersion
import com.domker.base.addItemDecoration
import com.domker.base.file.FileUtils
import com.domker.base.toChinese
import com.google.android.material.snackbar.Snackbar
import com.king.image.imageviewer.ImageViewer
import com.king.image.imageviewer.loader.GlideImageLoader


class HomeFragment : Fragment() {
    private lateinit var adapter: AppDetailAdapter
    private lateinit var homeViewModel: HomeViewModel
    private var appPackageName: String? = null
    private var apkSourcePath: String? = null

    private var detailList = mutableListOf<AppItemInfo>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = AppDetailActivity.homeViewModel
            ?: ViewModelProvider(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_detail_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtonListener(view)
        initView(view)
        initObserver(view)
    }

    private fun initView(root: View) {
        val context = requireContext()

        // recyclerview
        val recyclerViewAppInfo: RecyclerView = root.findViewById(R.id.recyclerViewAppInfo)
        recyclerViewAppInfo.layoutManager = LinearLayoutManager(context)
        recyclerViewAppInfo.addItemDecoration(context, R.drawable.inset_recyclerview_divider)
        recyclerViewAppInfo.setItemViewCacheSize(100)
        adapter = AppDetailAdapter(context, AppDetailItemDiffCallBack())
        adapter.setOnItemClickListener { view, position ->
            if (adapter.getItemViewType(position) == TYPE_PACKAGE) {
                openPackageExplorer()
            }
        }
        recyclerViewAppInfo.adapter = adapter
    }

    private fun initButtonListener(root: View) {
        val context = requireContext()
        root.findViewById<Button>(R.id.buttonStart).setOnClickListener {
            launchApp(it, context)
        }

        root.findViewById<Button>(R.id.buttonApkExplorer).setOnClickListener {
            openPackageExplorer()
        }

        root.findViewById<Button>(R.id.buttonUninstall).setOnClickListener {
            uninstallApp(it)
        }

        root.findViewById<Button>(R.id.buttonSettings).setOnClickListener {
            appPackageName?.let {
                openAppSetting(it)
            }
        }

    }

    private fun uninstallApp(view: View) {
        Snackbar.make(view, R.string.action_uninstall_app, Snackbar.LENGTH_LONG)
            .setAction(R.string.uninstall) {
                appPackageName?.apply {
                    val uri: Uri = Uri.parse("package:$this")
                    val intent = Intent(Intent.ACTION_DELETE, uri)
                    startActivity(intent)
                }
            }.show()
    }

    /**
     * 启动App
     */
    private fun launchApp(view: View, context: Context) {
        Snackbar.make(view, R.string.action_launch_app, Snackbar.LENGTH_LONG)
            .setAction(R.string.launch) {
                appPackageName?.apply {
                    val intent = IntentUtil.createLaunchIntent(context, this)
                    startActivity(intent)
                }
            }.show()
    }

    private fun openPackageExplorer() {
        apkSourcePath?.apply {
            ARouter.getInstance()
                .build(Router.EXPLORER_ACTIVITY)
                .withString("apk_source_path", this)
                .withString("package_name", appPackageName)
                .navigation()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.detail_home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_start_app -> {
                launchApp(requireView(), requireContext())
                true
            }
            R.id.menu_uninstall_app -> {
                uninstallApp(requireView())
                true
            }
            R.id.menu_setting_app -> {
                appPackageName?.let {
                    openAppSetting(it)
                }
                true
            }
            R.id.menu_explorer_app -> {
                openPackageExplorer()
                true
            }
            R.id.menu_export_app -> {
                exportApp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * 打开app设置
     */
    private fun openAppSetting(packageName: String) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", packageName, null)
        startActivity(intent)
    }

    private fun exportApp() {
        apkSourcePath?.let {

        }
    }

    private fun initObserver(root: View) {
        val appName: TextView = root.findViewById(R.id.app_name)
        val appPackage: TextView = root.findViewById(R.id.app_package)
        val appIcon: ImageView = root.findViewById(R.id.app_icon)

        homeViewModel.appInfo.observe(viewLifecycleOwner, {
            appPackageName = it.packageName
            apkSourcePath = it.sourceDir
            appName.text = "${it.appName} (${it.versionName})"
            appPackage.text = it.packageName
            appIcon.setImageDrawable(it.iconDrawable)
            appIcon.setOnClickListener { v ->
                //图片查看器
                // data 可以多张图片List或单张图片，支持的类型可以是{@link Uri}, {@code url}, {@code path},{@link File}, {@link DrawableRes resId}…等
                ImageViewer.load(it.iconDrawable!!) //要加载的图片数据，单张或多张
//                        .selection(position) //当前选中位置
//                        .indicator(true) //是否显示指示器，默认不显示
                    .imageLoader(GlideImageLoader()) //加载器，*必须配置，目前内置的有GlideImageLoader或PicassoImageLoader，也可以自己实现
                    //                      .imageLoader(new PicassoImageLoader())
                    .theme(R.style.ImageViewerTheme) //设置主题风格，默认：R.style.ImageViewerTheme
                    .orientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) //设置屏幕方向,默认：ActivityInfo.SCREEN_ORIENTATION_BEHIND
                    .start(this, v)
            }

            detailList.add(AppItemInfo("版本名", it.versionName))
            detailList.add(AppItemInfo("版本号", it.versionCode.toString()))
            val t = SystemVersion.getVersion(it.targetSdkVersion)
            detailList.add(AppItemInfo("目标版本号", SystemVersion.getShowLabel(t)))
            val v = SystemVersion.getVersion(it.minSdkVersion)
            detailList.add(AppItemInfo("最低支持系统版本", SystemVersion.getShowLabel(v)))
            detailList.add(AppItemInfo("系统应用", it.isSystemApp.toChinese()))
            detailList.add(AppItemInfo("首次安装时间", DataFormat.getAppInstallTime(it.installTime)))
            detailList.add(AppItemInfo("最近更新时间", DataFormat.getDataFromTimestamp(it.updateTime)))
            detailList.add(AppItemInfo("Application名称", it.applicationName!!))
            detailList.add(AppItemInfo("源文件路径", it.sourceDir!!, type = TYPE_PACKAGE))
            detailList.add(AppItemInfo("源文件大小", FileUtils.formatFileSize(it.sourceApkSize!!)))
            detailList.add(AppItemInfo("Native库路径", it.nativeLibraryDir!!))
            detailList.add(AppItemInfo("主要CPU架构", it.primaryCpuAbi ?: ""))
            detailList.add(AppItemInfo("Data路径", it.dataDir!!))
            detailList.add(AppItemInfo("主进程名", it.processName!!))
            detailList.add(AppItemInfo("SHA256签名", it.signature ?: ""))
            detailList.add(AppItemInfo("User ID", it.uid.toString()))
            detailList.add(AppItemInfo("Flag", it.flag ?: "Unknown"))

            adapter.setDetailList(detailList)
            adapter.notifyDataSetChanged()
        })
    }
}
