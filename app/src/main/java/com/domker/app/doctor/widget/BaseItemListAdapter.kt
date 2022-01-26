package com.domker.app.doctor.widget

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView

/**
 * ItemList类型的列表中，有一些共性的特征，抽象成基类实现
 */
abstract class BaseItemListAdapter<VH : RecyclerView.ViewHolder>(context: Context) : RecyclerView.Adapter<VH>() {
    protected val inflater: LayoutInflater = LayoutInflater.from(context)

    // 每个Item点击的监听器
    private var itemListener: ((VH, Int) -> Unit)? = null

    /**
     * 设置点击Item的函数
     */
    fun itemClick(click: (VH, Int) -> Unit) {
        itemListener = click
    }

    /**
     * 执行Item点击事件
     */
    protected fun invokeItemClick(vh: VH, position: Int) {
        itemListener?.let { it -> it(vh, position) }
    }
}