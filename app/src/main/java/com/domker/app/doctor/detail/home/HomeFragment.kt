package com.domker.app.doctor.detail.home

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.app.doctor.R
import com.domker.app.doctor.detail.AppDetailListAdapter
import com.domker.app.doctor.util.IntentUtil
import com.domker.app.doctor.util.Router
import com.domker.base.addDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.king.image.imageviewer.ImageViewer
import com.king.image.imageviewer.loader.GlideImageLoader

/**
 * App详情信息里面的首页
 */
class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var mListAdapter: AppDetailListAdapter

    private var appPackageName: String? = null
    private var apkSourcePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        recyclerViewAppInfo.addDividerItemDecoration(context, R.drawable.inset_recyclerview_divider)
        recyclerViewAppInfo.setItemViewCacheSize(100)
        mListAdapter = AppDetailListAdapter(context)
        recyclerViewAppInfo.adapter = mListAdapter
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
                    startActivity(IntentUtil.createUninstallAppIntent(this))
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
                    startActivity(IntentUtil.createLaunchIntent(context, this))
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
        startActivity(IntentUtil.createOpenSettingIntent(packageName))
    }

    /**
     * 导出APK到指定位置
     */
    private fun exportApp() {
        apkSourcePath?.let {

        }
    }

    private fun initObserver(root: View) {
        val appName: TextView = root.findViewById(R.id.app_name)
        val appPackage: TextView = root.findViewById(R.id.app_package)
        val appIcon: ImageView = root.findViewById(R.id.app_icon)

        homeViewModel.getAppInfo().observe(viewLifecycleOwner) {
            val a = it.appEntity
            appPackageName = a.packageName
            apkSourcePath = a.sourceDir
            appName.text = "${a.appName} (${a.versionName})"
            appPackage.text = a.packageName
            appIcon.setImageDrawable(a.iconDrawable)
            appIcon.setOnClickListener { v ->
                //图片查看器
                // data 可以多张图片List或单张图片，支持的类型可以是{@link Uri}, {@code url}, {@code path},{@link File}, {@link DrawableRes resId}…等
                ImageViewer.load(a.iconDrawable!!) //要加载的图片数据，单张或多张
//                        .selection(position) //当前选中位置
//                        .indicator(true) //是否显示指示器，默认不显示
                    .imageLoader(GlideImageLoader()) //加载器，*必须配置，目前内置的有GlideImageLoader或PicassoImageLoader，也可以自己实现
                    //                      .imageLoader(new PicassoImageLoader())
                    .theme(R.style.ImageViewerTheme) //设置主题风格，默认：R.style.ImageViewerTheme
                    .orientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) //设置屏幕方向,默认：ActivityInfo.SCREEN_ORIENTATION_BEHIND
                    .start(this, v)
            }
            mListAdapter.setDetailList(it.itemList)
            mListAdapter.notifyItemRangeChanged(0, it.itemList.size)
        }
    }
}
