package com.domker.app.doctor.detail.container

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.domker.app.doctor.R
import com.domker.app.doctor.detail.DetailItemViewHolder
import com.domker.app.doctor.entiy.AppItemInfo

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