package com.domker.doctor.app.detail.home

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.doctor.R
import com.domker.doctor.databinding.FragmentDetailMainBinding
import com.domker.doctor.data.db.AppEntity
import com.domker.doctor.util.IntentUtil
import com.domker.doctor.util.PathUtils
import com.domker.doctor.util.Router
import com.domker.doctor.base.file.AppFileUtils
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
            AppExporter.exportFile(sourceFile, destFilePath).collect {
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
     * 调用系统分享app
     */
    fun shareApp(context: Context) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            File(apkSourcePath!!)
        )
        //传输图片或者文件 采用流的方式
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.type = "*/*" //分享文件
        context.startActivity(Intent.createChooser(intent, "分享"))
    }

    /**
     * 调用系统分享
     */
    private fun invokeSystemShare(context: Context) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        //传输图片或者文件 采用流的方式
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(File(apkSourcePath!!)))
        intent.type = "*/*" //分享文件
        context.startActivity(Intent.createChooser(intent, "分享"))
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