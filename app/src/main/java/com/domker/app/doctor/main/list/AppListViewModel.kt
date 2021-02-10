package com.domker.app.doctor.main.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.domker.app.doctor.data.AppEntity

/**
 * App列表数据
 * Created by wanlipeng on 1/29/21 3:52 PM
 */
class AppListViewModel : ViewModel() {
    private val _appList = MutableLiveData<List<AppEntity>>()

    val appListData = _appList
}