package com.domker.doctor.app.detail.container

import android.view.LayoutInflater
import com.domker.doctor.app.detail.DetailItemViewHolder

/**
 * 详细页面中，需要进一步查看的Item类型比较多，并且有扩展的情况，所以针对性的创建管理器，业务隔离，做创建和绑定的隔离
 */
class DetailContainerManager(private val inflater: LayoutInflater) {
    private val containers: MutableMap<Int, DetailContainer<DetailItemViewHolder>> = mutableMapOf()

    /**
     * 根据类型，创建一个Container
     */
    fun obtain(viewType: Int): DetailContainer<DetailItemViewHolder> {
        if (containers[viewType] == null) {
            containers[viewType] = createContainer(viewType)
        }
        return containers[viewType]!!
    }

    private fun createContainer(viewType: Int): DetailContainer<DetailItemViewHolder> {
        return when (viewType) {
            DETAIL_TYPE_SUBJECT_CONTENT -> SubjectContentContainer(inflater)
            DETAIL_TYPE_PACKAGE -> PackageContainer(inflater)
            DETAIL_TYPE_SIGNATURE -> SignatureContainer(inflater)
            else -> SubjectContentContainer(inflater)
        }
    }
}