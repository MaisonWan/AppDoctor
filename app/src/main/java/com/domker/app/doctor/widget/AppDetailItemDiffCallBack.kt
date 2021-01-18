package com.domker.app.doctor.widget

import androidx.recyclerview.widget.DiffUtil
import com.domker.app.doctor.entiy.AppItemInfo

/**
 * 详情页里面，每个Item的去重
 *
 * Created by wanlipeng on 2019-12-16 18:14
 */
class AppDetailItemDiffCallBack : DiffUtil.ItemCallback<AppItemInfo>() {

    override fun areItemsTheSame(oldItem: AppItemInfo, newItem: AppItemInfo): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AppItemInfo, newItem: AppItemInfo): Boolean {
//        return oldItem.type == newItem.type && oldItem.subject == newItem.subject && oldItem.showLabel == newItem.showLabel
        return oldItem.subject == newItem.subject
    }

}