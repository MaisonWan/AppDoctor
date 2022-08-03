package com.domker.app.doctor.detail.home

import android.content.pm.ActivityInfo
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.FragmentDetailMainBinding
import com.domker.app.doctor.db.AppEntity
import com.domker.app.doctor.util.IntentUtil
import com.domker.app.doctor.util.PathUtils
import com.domker.app.doctor.util.Router
import com.domker.base.file.AppFileUtils
import com.king.image.imageviewer.ImageViewer
import com.king.image.imageviewer.loader.GlideImageLoader
import kotlinx.coroutines.launch
import java.io.File

/**
 * 创建ViewModel
 * Created by wanlipeng on 2022/8/2 15:50
 */
class HomeViewModel : ViewModel() {
    private var progressData = MutableLiveData<Int>()
    private val status = MutableLiveData<ProgressStatus>()

    var fragment: Fragment? = null
    private var appPackageName: String? = null
    private var apkSourcePath: String? = null
    private var exportFileName: String? = null

    /**
     * 导出App文件
     */
    fun exportApp() {
        if (apkSourcePath == null) {
            return
        }
        val sourceFile = File(apkSourcePath!!)
        val destFilePath =
            File(PathUtils.exportAppPath(), exportFileName ?: "${System.currentTimeMillis()}.apk")

        viewModelScope.launch {
            val total = AppFileUtils.size(sourceFile.absolutePath)
            var current = 0
            AppExporter(fragment!!.requireContext()).exportFile(sourceFile, destFilePath).collect {
                if (it is ProgressStatus.Progress) {
                    val p = (it.progress * 100L / total).toInt()
                    // 避免同一个数字重复发
                    if (current != p) {
                        current = p
                        progressData.value = current
                    }
                } else {
                    status.postValue(it)
                }
            }
        }
    }

    /**
     * 设置监听下载的进度条
     */
    fun observerExport(lifecycleOwner: LifecycleOwner, progressBar: ProgressBar) {
        status.observe(lifecycleOwner) {
            when (it) {
                is ProgressStatus.Start -> {
                    progressBar.visibility = View.VISIBLE
                    progressBar.max = 100
                }
                is ProgressStatus.Done -> {
                    progressBar.visibility = View.INVISIBLE
                    toast("安装文件导出到${it.file.absolutePath}")
                }
                is ProgressStatus.Error -> {
                    progressBar.visibility = View.INVISIBLE
                    toast("导出发现异常 ${it.throwable}")
                }
                else -> {}
            }
        }
        progressData.observe(lifecycleOwner) {
            progressBar.progress = it
        }
    }

    private fun toast(content: String) {
        Toast.makeText(fragment!!.requireContext(), content, Toast.LENGTH_LONG).show()
    }

    /**
     * 绑定详细信息
     */
    fun bindIntroduceDetail(entity: AppEntity, binding: FragmentDetailMainBinding) {
        appPackageName = entity.packageName
        apkSourcePath = entity.sourceDir
        binding.appName.text = "${entity.appName} (${entity.versionName})"
        binding.appPackage.text = entity.packageName
        binding.appIcon.setImageDrawable(entity.iconDrawable)

        // 导出apk文件的名字，包含名字和版本
        exportFileName = "${entity.appName}_${entity.versionName}.apk"
    }

    /**
     * 打开图标的操作
     */
    fun openIconView(a: AppEntity, v: View?) {
        //图片查看器
        // data 可以多张图片List或单张图片，支持的类型可以是{@link Uri}, {@code url}, {@code path},{@link File}, {@link DrawableRes resId}…等
        ImageViewer.load(a.iconDrawable!!) //要加载的图片数据，单张或多张
            //                        .selection(position) //当前选中位置
            //                        .indicator(true) //是否显示指示器，默认不显示
            .imageLoader(GlideImageLoader()) //加载器，*必须配置，目前内置的有GlideImageLoader或PicassoImageLoader，也可以自己实现
            //                      .imageLoader(new PicassoImageLoader())
            .theme(R.style.ImageViewerTheme) //设置主题风格，默认：R.style.ImageViewerTheme
            .orientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) //设置屏幕方向,默认：ActivityInfo.SCREEN_ORIENTATION_BEHIND
            .start(fragment!!, v)
    }

    /**
     * 打开app设置
     */
    fun openAppSetting() {
        appPackageName?.let {
            fragment!!.startActivity(IntentUtil.createOpenSettingIntent(it))
        }
    }

    /**
     * 弹出卸载程序
     */
    fun uninstallApp() {
        appPackageName?.let {
            fragment!!.startActivity(IntentUtil.createUninstallAppIntent(it))
        }
    }

    /**
     * 启动App
     */
    fun launchApp() {
        appPackageName?.apply {
            IntentUtil.createLaunchIntent(fragment!!.requireContext(), this).also {
                fragment!!.startActivity(it)
            }
        }
    }

    /**
     * 打开自定义的Apk文件浏览器
     */
    fun openPackageExplorer() {
        apkSourcePath?.apply {
            ARouter.getInstance()
                .build(Router.EXPLORER_ACTIVITY)
                .withString("apk_source_path", this)
                .withString("package_name", appPackageName)
                .navigation()
        }
    }
}