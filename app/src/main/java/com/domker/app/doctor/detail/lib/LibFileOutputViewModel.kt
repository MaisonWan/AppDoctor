package com.domker.app.doctor.detail.lib

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *
 * Created by wanlipeng on 2022/4/15 18:47
 */
class LibFileOutputViewModel : ViewModel() {

    // 释放lib文件之后的本地路径
    val libFilePath = MutableLiveData<String>()

    /**
     * 释放文件，传入参数是压缩包里面的相对路径
     */
    fun outputFile(filePath: String) {

    }
}