package com.domker.doctor.overview

import android.content.Context
import com.domker.doctor.R
import com.domker.base.device.DeviceManager

/**
 * 设备屏幕信息
 */
class DeviceScreenBinder(val context: Context) : DeviceBinder {

    val deviceManager by lazy { DeviceManager(context) }
    override fun bind(holder: OverviewRecyclerViewAdapter.ViewHolder, item: DeviceItem) {
        holder.iconView?.setImageResource(R.drawable.baseline_screenshot_24)


    }
}