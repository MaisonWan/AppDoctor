package com.domker.app.doctor.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.domker.app.doctor.entiy.ActivityInfo

/**
 * AppInfo的ViewModel类
 * Created by wanlipeng on 2019-11-28 16:44
 */
class AppInfoViewModel : ViewModel() {
    private val appInfo = MutableLiveData<AppEntity>()

    //    private val activityInfo: LiveData<PagedList<ActivityInfo>>
    private val activityInfo = MutableLiveData<MutableList<ActivityInfo>>()
    private val serviceInfo = MutableLiveData<MutableList<ActivityInfo>>()
    private val dataSource: DataSource<Int, ActivityInfo>

    private val config = PagedList.Config.Builder()
            .setPageSize(20 / 2) // 分页加载的数量
            .setEnablePlaceholders(false) // 当item为null是否使用PlaceHolder展示
            .setInitialLoadSizeHint(15) // 预加载的数量, 与分页加载的数量成倍数关系
            .setPrefetchDistance(5)
            .build()

    init {
        val factory = AppDataSourceFactory()
        dataSource = factory.create()
//        activityInfo = LivePagedListBuilder(factory, config).build()
    }

    fun getAppInfo(): MutableLiveData<AppEntity> = appInfo

    fun getActivityInfo(): MutableLiveData<MutableList<ActivityInfo>> = activityInfo

    /**
     * 刷新数据源
     */
    fun invalidateDataSource() = dataSource.invalidate()

    fun getServiceInfo(): MutableLiveData<MutableList<ActivityInfo>> = serviceInfo
}