package com.domker.app.doctor.hardware.sensor

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.hardware.sensor.listener.OnViewPagerListener

/**
 * 实现翻页的布局管理器
 */
class PageLayoutManager(context: Context) : LinearLayoutManager(context) {

    private lateinit var pagerSnapHelper: PagerSnapHelper
    private var onViewPagerListener: OnViewPagerListener? = null
    private lateinit var recyclerView: RecyclerView

    //位移，用来判断移动方向
    private val drift = 0

    private val childAttachStateChangeListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
//            if (onViewPagerListener != null && childCount == 1) {
//                onViewPagerListener.onInitComplete()
//            }

        }

        override fun onChildViewDetachedFromWindow(view: View) {

        }

    }


    private fun initHelper() {
        pagerSnapHelper = PagerSnapHelper()
    }

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        pagerSnapHelper.attachToRecyclerView(view)

        recyclerView = view

//        recyclerView.addOnChildAttachStateChangeListener(childAttachStateChangeListener)
    }

//    override fun onScrollStateChanged(state: Int) {
//        super.onScrollStateChanged(state)
//
//        when (state) {
//            RecyclerView.SCROLL_STATE_IDLE -> {
//                pagerSnapHelper.findSnapView(this)?.let { view ->
//                    val positionIdle = getPosition(view)
//                    if (onViewPagerListener != null && childCount == 1) {
//                        onViewPagerListener.onPageSelected(positionIdle, positionIdle == itemCount - 1)
//                    }
//                }
//            }
//
//            RecyclerView.SCROLL_STATE_DRAGGING -> {
//                val viewDrag = pagerSnapHelper.findSnapView(this)
//                val positionDrag = getPosition(viewDrag)
//            }
//
//            RecyclerView.SCROLL_STATE_SETTLING -> {
//                val viewSettling = pagerSnapHelper.findSnapView(this)
//
//                val positionSettling = getPosition(viewSettling)
//            }
//        }
//    }
    init {
        initHelper()
    }
}