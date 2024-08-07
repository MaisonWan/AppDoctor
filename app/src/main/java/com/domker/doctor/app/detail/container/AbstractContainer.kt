package com.domker.doctor.app.detail.container

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * 抽象基类
 * @param viewType View类型
 */
abstract class AbstractContainer<T>(val inflater: LayoutInflater, private val viewType: Int) :
    DetailContainer<T> {
    private var containerListener: ((T, Int) -> Unit)? = null

    protected fun createView(layoutId: Int, parent: ViewGroup): View {
        return inflater.inflate(layoutId, parent, false)
    }

    override fun getViewType(): Int {
        return viewType
    }

    override fun setContainerClickListener(listener: ((T, Int) -> Unit)?) {
        containerListener = listener
    }

    /**
     * 执行这个回调器
     */
    protected fun invokeClickListener(t: T, position: Int) {
        containerListener?.let { it(t, position) }
    }
}