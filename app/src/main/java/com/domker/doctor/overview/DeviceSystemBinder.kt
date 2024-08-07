package com.domker.doctor.overview

import android.os.Build
import com.domker.doctor.R
import com.domker.base.SystemVersion

class DeviceSystemBinder : DeviceBinder {
    override fun bind(holder: OverviewRecyclerViewAdapter.ViewHolder, item: DeviceItem) {
        holder.iconView?.setImageResource(R.drawable.baseline_android_24)

        val v = SystemVersion.getVersion(Build.VERSION.SDK_INT)
        holder.mainTextView?.text = "Android ${v.version}"
        holder.secondaryTextView?.text = "API ${v.api}"
    }
}