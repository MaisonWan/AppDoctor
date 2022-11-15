package com.domker.app.doctor.main.applist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.domker.app.doctor.data.AppCheckFactory
import com.domker.app.doctor.db.AppEntity
import com.domker.base.thread.AppExecutors

/**
 * App列表数据
 * Created by wanlipeng on 1/29/21 3:52 PM
 */
class AppListViewModel : ViewModel() {
    private val appList = MutableLiveData<List<AppEntity>>()

    /**
     * 返回app列表数据
     */
    fun getAppList(): LiveData<List<AppEntity>> = appList


    /**
     * 获取最新的信息，是否包含系统应用
     */
    fun updateAppList(allApp: Boolean) {
        val app = AppCheckFactory.instance

        AppExecutors.executor.execute {
            val newAppList = app.getAppList(allApp)
            appList.postValue(newAppList)
        }
    }

}