package com.domker.doctor.overview

import android.content.Context
import android.os.Build
import android.widget.Toast
import com.domker.base.device.DeviceManager
import com.domker.doctor.R
import com.domker.doctor.overview.OverviewRecyclerViewAdapter.ViewHolder
import java.text.DecimalFormat

/**
 * 绑定Model信息操作类
 */
class DeviceBasicBinder(private val context: Context) : DeviceBinder {

    private val deviceManager = DeviceManager(context)

    private var type = DeviceItem.DEVICE_TYPE_MODEL

    override fun bind(holder: ViewHolder, item: DeviceItem) {
        when (this.type) {
            DeviceItem.DEVICE_TYPE_MODEL -> bindModel(holder)
            DeviceItem.DEVICE_TYPE_SCREEN -> bindScreen(holder)
        }

    }

    private fun bindScreen(holder: ViewHolder) {
        val size = deviceManager.getScreenSize()

        holder.itemView.setOnClickListener {
            Toast.makeText(context, "click screen", Toast.LENGTH_SHORT).show()
        }

        holder.iconView?.setImageResource(R.drawable.baseline_screenshot_24)
        holder.mainTextView?.text = context.getString(R.string.screen)

        val format = DecimalFormat("0.##")
        val s = format.format(deviceManager.getPhysicsScreenSize())
        val rate = deviceManager.getRefreshRate()
        holder.secondaryTextView?.text = "分辨率：${size.first} x ${size.second}\n尺寸：${s}″ | $rate Hz"
    }

    private fun bindModel(holder: ViewHolder) {
        holder.iconView?.setImageResource(R.drawable.baseline_phone_android_24)

        holder.mainTextView?.text = Build.MODEL
        holder.secondaryTextView?.text = Build.DEVICE

        holder.itemView.setOnClickListener {
            Toast.makeText(context, "click model", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * 通过指定类型来分别绑定信息
     */
    fun bindByType(holder: ViewHolder, item: DeviceItem, type: Int) {
        this.type = type
        bind(holder, item)
    }
}