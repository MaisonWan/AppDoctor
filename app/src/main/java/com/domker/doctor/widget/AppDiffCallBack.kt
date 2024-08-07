package com.domker.doctor.widget

import androidx.recyclerview.widget.DiffUtil
import com.domker.doctor.db.AppEntity

/**
 * 差分更新的类
 * Created by wanlipeng on 2018/2/6.
 */
class AppDiffCallBack(private var oldList: List<AppEntity>?, private var newList: List<AppEntity>?) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldList == null || newList == null) {
            return false
        }
        return oldList!![oldItemPosition] == newList!![newItemPosition]
    }

    override fun getOldListSize(): Int = oldList?.size ?: 0

    override fun getNewListSize(): Int = newList?.size ?: 0

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldList == null || newList == null) {
            return false
        }
        return oldList!![oldItemPosition].packageName == newList!![newItemPosition].packageName
    }

}