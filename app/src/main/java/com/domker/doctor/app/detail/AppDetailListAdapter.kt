package com.domker.doctor.app.detail

import android.content.Context
import android.view.ViewGroup
import com.domker.doctor.app.detail.container.DetailContainerManager
import com.domker.doctor.entiy.AppItemInfo
import com.domker.doctor.widget.BaseItemListAdapter

/**
 * 应用详情中需要按条展示的适配器，可用于多种场景，使用Type类型注入，然后绑定分发
 */
class AppDetailListAdapter(context: Context) :
    BaseItemListAdapter<DetailItemViewHolder>(context) {
    // 展示数据
    private var mDetailItemList: MutableList<AppItemInfo> = mutableListOf()
    private val containerManager: DetailContainerManager = DetailContainerManager(inflater)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailItemViewHolder {
        val container = containerManager.obtain(viewType)
        return DetailItemViewHolder(container.createContainerView(parent))
    }

    override fun getItemCount(): Int = mDetailItemList.size

    override fun onBindViewHolder(holder: DetailItemViewHolder, position: Int) {
        val item = getItem(position)
        val container = containerManager.obtain(getItemViewType(position))
        container.bindViewHolder(holder, item, position)
    }

    override fun getItemViewType(position: Int): Int {
        return mDetailItemList[position].type
    }

    private fun getItem(position: Int): AppItemInfo {
        return mDetailItemList[position]
    }

    /**
     * 更新数据
     */
    fun setDetailList(detailList: List<AppItemInfo>) {
        mDetailItemList.clear()
        mDetailItemList.addAll(detailList)
    }

}