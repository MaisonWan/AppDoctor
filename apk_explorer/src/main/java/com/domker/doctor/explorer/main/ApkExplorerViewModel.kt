package com.domker.doctor.explorer.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.domker.base.file.ZipFileItem

/**
 *
 * Created by wanlipeng on 2020/11/9 9:15 PM
 */
class ApkExplorerViewModel : ViewModel() {
    private val _apkInfo = MutableLiveData<List<ZipFileItem>>()

    val appInfo: MutableLiveData<List<ZipFileItem>> = _apkInfo
}