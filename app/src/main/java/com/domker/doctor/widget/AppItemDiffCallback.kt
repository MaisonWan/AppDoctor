package com.domker.doctor.widget

import androidx.recyclerview.widget.DiffUtil
import com.domker.doctor.entiy.AppItemInfo

/**
 * DiffUtil回调
 * Created by wanlipeng on 2019-12-01 21:34
 */
class AppItemDiffCallback(private val oldList: List<AppItemInfo>,
                          private val newList: List<AppItemInfo>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newItem = newList[newItemPosition]
        val oldItem = oldList[oldItemPosition]
        return newItem == oldItem
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newItem = newList[newItemPosition]
        val oldItem = oldList[oldItemPosition]
        return newItem.type == oldItem.type && newItem.content == oldItem.content
    }
}