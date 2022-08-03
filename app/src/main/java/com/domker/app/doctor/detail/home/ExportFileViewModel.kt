package com.domker.app.doctor.detail.home

import android.app.Application
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.domker.base.file.AppFileUtils
import kotlinx.coroutines.launch
import java.io.File

/**
 * 创建ViewModel
 * Created by wanlipeng on 2022/8/2 15:50
 */
class ExportFileViewModel(application: Application) : AndroidViewModel(application) {
    private var progressData = MutableLiveData<Int>()
    private val status = MutableLiveData<ProgressStatus>()

    /**
     * 导出文件
     */
    fun export(sourceFile: File, destFile: File) {
        viewModelScope.launch {
            val total = AppFileUtils.size(sourceFile.absolutePath)
            var current = 0
            AppExporter(getApplication()).exportFile(sourceFile, destFile).collect {
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
    fun observer(lifecycleOwner: LifecycleOwner, progressBar: ProgressBar) {
        status.observe(lifecycleOwner) {
            when (it) {
                is ProgressStatus.Start -> {
                    progressBar.visibility = View.VISIBLE
                    progressBar.max = 100
                }
                is ProgressStatus.Done -> {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(getApplication(), "安装文件导出到${it.file.absolutePath}", Toast.LENGTH_LONG).show()
                }
                is ProgressStatus.Error -> {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(getApplication(), "导出发现异常 ${it.throwable}", Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
        progressData.observe(lifecycleOwner) {
            progressBar.progress = it
        }
    }
}