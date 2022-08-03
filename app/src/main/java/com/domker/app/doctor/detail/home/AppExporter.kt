package com.domker.app.doctor.detail.home

import android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager
import com.domker.base.copyTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * 文件导出的操作
 * Created by wanlipeng on 2022/7/26 17:19
 */
class AppExporter(private val context: Context) {

    /**
     * 使用Flow的方式拷贝文件
     */
    fun exportFile(sourceFile: File, destFile: File): Flow<ProgressStatus> {
        // 检查一下文件夹是否存在
        destFile.parentFile?.let {
            checkFolder(it)
        }
        val source = FileInputStream(sourceFile)
        val dest = FileOutputStream(destFile)

        return flow {
            // 开始传输
            emit(ProgressStatus.Start(sourceFile))
            source.copyTo(dest) { currentLength ->
                emit(ProgressStatus.Progress(currentLength))
            }
            emit(ProgressStatus.Done(destFile))
        }.catch {
            destFile.delete()
            emit(ProgressStatus.Error(it))
        }.flowOn(Dispatchers.IO)
    }

    private fun checkFolder(folder: File) {
        if (!folder.exists()) {
            folder.mkdir()
        }
    }

    /**
     * 检测是否有写入的权限
     */
    fun checkPermission(): Boolean {
        getPermissions().forEach {
            if (context.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun getPermissions(): Array<String> {
        return arrayOf(MANAGE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
    }

    /**
     * 权限请求的代码
     */
    fun getPermissionRequestCode(): Int = 1010
}