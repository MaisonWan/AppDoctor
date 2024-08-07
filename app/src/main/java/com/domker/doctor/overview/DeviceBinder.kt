package com.domker.doctor.overview

import com.domker.doctor.overview.OverviewRecyclerViewAdapter.ViewHolder


interface DeviceBinder {

    /**
     * 绑定视图的数据
     */
    fun bind(holder: ViewHolder, item: DeviceItem)
}