package com.domker.doctor.app.detail.home

import com.domker.doctor.base.copyTo
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
object AppExporter {

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
}