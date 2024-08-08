package com.domker.doctor.app.detail.container

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.domker.doctor.R
import com.domker.doctor.app.detail.DetailItemViewHolder
import com.domker.doctor.app.entiy.AppItemInfo

/**
 * 标题和内容的容器
 */
class SubjectContentContainer(inflater: LayoutInflater) :
    AbstractContainer<DetailItemViewHolder>(inflater, DETAIL_TYPE_SUBJECT_CONTENT) {

    override fun createContainerView(parent: ViewGroup): View {
        return createView(R.layout.item_subject_content, parent)
    }

    override fun bindViewHolder(holder: DetailItemViewHolder, data: AppItemInfo, position: Int) {
        holder.content?.text = data.content
        holder.subject?.text = data.subject
    }
}