package com.domker.app.doctor.data

import androidx.paging.PositionalDataSource
import com.domker.app.doctor.entiy.ActivityInfo
import com.domker.app.doctor.util.log


/**
 * 分页里面展示的时候，需要的数据源
 * Created by wanlipeng on 2019-12-09 15:09
 */
class ActivityDataSource : PositionalDataSource<ActivityInfo>() {

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<ActivityInfo>) {
        log("ActivityDataSource loadRange ${params.startPosition} ${params.loadSize}")

        callback.onResult(fetchItems(params.startPosition, params.loadSize))
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<ActivityInfo>) {
        val requestedLoadSize = params.requestedLoadSize
        log("ActivityDataSource loadInitial $requestedLoadSize ${params.pageSize}")

        callback.onResult(fetchItems(0, params.requestedLoadSize), 0)
    }

    private fun fetchItems(startPosition: Int, size: Int): List<ActivityInfo> {
        val list = mutableListOf<ActivityInfo>()
        (startPosition until startPosition + size).forEach {
            val a = ActivityInfo()
            a.name = "activity $it"
            list.add(a)
        }
        return list
    }
}