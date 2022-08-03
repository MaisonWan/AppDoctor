package com.domker.app.doctor.detail.home

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.FragmentDetailMainBinding
import com.domker.app.doctor.db.AppEntity
import com.domker.app.doctor.detail.AppDetailListAdapter
import com.domker.app.doctor.util.IntentUtil
import com.domker.app.doctor.util.PathUtils
import com.domker.app.doctor.util.Router
import com.domker.base.addDividerItemDecoration
import com.domker.base.file.StoragePermissionUtil
import com.google.android.material.snackbar.Snackbar
import com.king.image.imageviewer.ImageViewer
import com.king.image.imageviewer.loader.GlideImageLoader
import java.io.File

/**
 * App详情信息里面的首页
 */
class HomeFragment : Fragment(), MenuProvider {
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var exportFileViewModel: ExportFileViewModel
    private lateinit var binding: FragmentDetailMainBinding
    private lateinit var mListAdapter: AppDetailListAdapter

    private var appPackageName: String? = null
    private var apkSourcePath: String? = null
    private var exportFileName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StoragePermissionUtil.registerForActivityResult(requireActivity())
        exportFileViewModel = ViewModelProvider(this)[ExportFileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtonListener()
        initView()
        initObserver()
        initMenu()
    }

    private fun initView() {
        val context = requireContext()

        // recyclerview
        binding.recyclerViewAppInfo.also { r ->
            r.layoutManager = LinearLayoutManager(context)
            r.addDividerItemDecoration(context, R.drawable.inset_recyclerview_divider)
            r.setItemViewCacheSize(100)
            mListAdapter = AppDetailListAdapter(context)
            r.adapter = mListAdapter
        }
    }

    private fun initButtonListener() {
        val context = requireContext()
        binding.buttonLayout.buttonStart.setOnClickListener {
            launchApp(it, context)
        }

        binding.buttonLayout.buttonApkExplorer.setOnClickListener {
            openPackageExplorer()
        }

        binding.buttonLayout.buttonUninstall.setOnClickListener {
            uninstallApp(it)
        }

        binding.buttonLayout.buttonSettings.setOnClickListener {
            appPackageName?.let {
                openAppSetting(it)
            }
        }

    }

    private fun initMenu() {
        (activity as ComponentActivity).addMenuProvider(this, viewLifecycleOwner)
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
    private fun launchApp(view: View, context: Context): Boolean {
        Snackbar.make(view, R.string.action_launch_app, Snackbar.LENGTH_LONG)
            .setAction(R.string.launch) {
                appPackageName?.apply {
                    startActivity(IntentUtil.createLaunchIntent(context, this))
                }
            }.show()
        return true
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

    private fun menuActions(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_start_app -> launchApp(requireView(), requireContext())
            R.id.menu_uninstall_app -> uninstallApp(requireView())
            R.id.menu_setting_app -> appPackageName?.let { openAppSetting(it) }
            R.id.menu_explorer_app -> openPackageExplorer()
            R.id.menu_export_app -> exportApp()
        }
        return true
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
        StoragePermissionUtil.requestStoragePermission(requireActivity()) {
            apkSourcePath?.let { path ->
                val destFilePath = File(PathUtils.exportAppPath(), exportFileName ?: "${System.currentTimeMillis()}.apk")
                exportFileViewModel.export(File(path), destFilePath)
            }
        }
    }

    private fun initObserver() {
        homeViewModel.getAppInfo().observe(viewLifecycleOwner) {
            bindIntroDetail(it.appEntity, binding.appName, binding.appPackage, binding.appIcon)
            mListAdapter.setDetailList(it.itemList)
            mListAdapter.notifyItemRangeChanged(0, it.itemList.size)
        }

        // 导出文件的进度条
        exportFileViewModel.observer(viewLifecycleOwner, binding.progressBarExport)
    }

    private fun bindIntroDetail(
        entity: AppEntity,
        appName: TextView,
        appPackage: TextView,
        appIcon: ImageView
    ) {
        appPackageName = entity.packageName
        apkSourcePath = entity.sourceDir
        appName.text = "${entity.appName} (${entity.versionName})"
        appPackage.text = entity.packageName
        appIcon.setImageDrawable(entity.iconDrawable)
        appIcon.setOnClickListener { v ->
            openIconView(entity, v)
        }

        // 导出apk文件的名字，包含名字和版本
        exportFileName = "${entity.appName}_${entity.versionName}.apk"
    }

    private fun openIconView(a: AppEntity, v: View?) {
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

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.detail_home_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return menuActions(menuItem)
    }
}
