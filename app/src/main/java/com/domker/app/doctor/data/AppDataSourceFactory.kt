package com.domker.app.doctor.data

import androidx.paging.DataSource
import com.domker.app.doctor.entiy.ActivityInfo

/**
 * 数据源创建工厂
 * Created by wanlipeng on 2019-12-09 17:27
 */
class AppDataSourceFactory : DataSource.Factory<Int, ActivityInfo>() {
//    private val liveData = MutableLiveData<ActivityDataSource>()

    override fun create(): DataSource<Int, ActivityInfo> = ActivityDataSource()

}