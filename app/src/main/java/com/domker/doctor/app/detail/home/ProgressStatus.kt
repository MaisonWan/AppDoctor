package com.domker.doctor.app.detail.home

import java.io.File

/**
 * 用于传输，下载文件时标记进度状态的类
 */
sealed class ProgressStatus {
    /**
     * 当前进展
     */
    data class Progress(val progress: Long) : ProgressStatus()

    /**
     * 出现什么错误
     */
    data class Error(val throwable: Throwable) : ProgressStatus()

    /**
     * 完成传输
     */
    data class Done(val file: File) : ProgressStatus()

    /**
     * 开始传输
     */
    data class Start(val file: File) : ProgressStatus()
}