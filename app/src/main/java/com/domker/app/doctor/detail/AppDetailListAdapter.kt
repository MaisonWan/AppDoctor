package com.domker.app.doctor.detail

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.domker.app.doctor.R
import com.domker.app.doctor.entiy.AppItemInfo
import com.domker.app.doctor.widget.BaseItemListAdapter

/**
 * 应用详情中需要按条展示的适配器，可用于多种场景，使用Type类型注入，然后绑定分发
 */
class AppDetailListAdapter(context: Context) :
    BaseItemListAdapter<DetailItemViewHolder>(context) {
    // 展示数据
    private var mDetailItemList: MutableList<AppItemInfo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailItemViewHolder {
        val layoutResId = when (viewType) {
            AppItemInfo.TYPE_SUBJECT_CONTENT, AppItemInfo.TYPE_SIGNATURE -> R.layout.item_subject_content
            AppItemInfo.TYPE_SUBJECT -> R.layout.item_subject
            AppItemInfo.TYPE_PACKAGE -> R.layout.item_detail_package
            else -> R.layout.detail_label_layout
        }
        val view: View = inflater.inflate(layoutResId, parent, false)
        return DetailItemViewHolder(view)
    }

    override fun getItemCount(): Int = mDetailItemList.size

    override fun onBindViewHolder(holder: DetailItemViewHolder, position: Int) {
        val item = mDetailItemList[position]
        holder.content?.text = item.content
        holder.subject?.text = item.subject

        when (getItemViewType(position)) {
            AppItemInfo.TYPE_SUBJECT_CONTENT -> bindSubjectContent(holder, item, position)
            AppItemInfo.TYPE_PACKAGE -> bindPackage(holder, position)
            AppItemInfo.TYPE_SIGNATURE -> bindSignature(holder, position)
        }
    }

    private fun bindSignature(holder: DetailItemViewHolder, position: Int) {
        holder.icon?.let {
            it.visibility = View.VISIBLE
            it.setImageResource(R.drawable.ic_baseline_article_24)
        }
    }

    private fun bindSubjectContent(holder: DetailItemViewHolder, appItemInfo: AppItemInfo, position: Int) {
        holder.itemView.setOnClickListener {
            invokeItemClick(holder, position)
        }
    }

    private fun bindPackage(holder: DetailItemViewHolder, position: Int) {
        holder.itemView.findViewById<Button>(R.id.buttonPackage).setOnClickListener {
            invokeItemClick(holder, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return mDetailItemList[position].type
    }

    /**
     * 更新数据
     */
    fun setDetailList(detailList: List<AppItemInfo>) {
        mDetailItemList.clear()
        mDetailItemList.addAll(detailList)
    }

    fun getItemList(): List<AppItemInfo> = mDetailItemList
}