package com.domker.doctor.app.detail.container

import android.view.View
import android.view.ViewGroup
import com.domker.doctor.app.entiy.AppItemInfo

/**
 * 有标题和内容的Item
 */
const val DETAIL_TYPE_SUBJECT_CONTENT = 1

/**
 * 只展示标题
 */
const val DETAIL_TYPE_SUBJECT = 2

/**
 * 包体积专用
 */
const val DETAIL_TYPE_PACKAGE = 3

/**
 * 签名专用类型
 */
const val DETAIL_TYPE_SIGNATURE = 4

interface DetailContainer<VH> {

    /**
     * 实现创建View
     */
    fun createContainerView(parent: ViewGroup): View

    /**
     * View类型
     */
    fun getViewType(): Int

    /**
     * 绑定具体的ViewHolder
     * @param holder
     * @param data
     * @param position
     */
    fun bindViewHolder(
        holder: VH,
        data: AppItemInfo,
        position: Int
    )

    /**
     * 设置容器点击的监听器
     */
    fun setContainerClickListener(listener: ((VH, Int) -> Unit)?)

}