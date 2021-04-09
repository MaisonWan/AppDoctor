package com.domker.app.doctor.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R
import com.domker.app.doctor.entiy.AppItemInfo
import com.domker.app.doctor.util.log

/**
 * 应用详情的适配器
 */
class AppDetailAdapter(context: Context, diffCallback: DiffUtil.ItemCallback<AppItemInfo>) :
        PagedListAdapter<AppItemInfo, AppDetailAdapter.AppDetailViewHolder>(diffCallback) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var mListener: ((view: View, position: Int) -> Unit)? = null
    private var mDetailItemList: List<AppItemInfo>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppDetailViewHolder {
        log("onCreateViewHolder $viewType")
        val layoutResId = when (viewType) {
            AppItemInfo.TYPE_SUBJECT_LABEL -> R.layout.item_detail_subject
            AppItemInfo.TYPE_SUBJECT -> R.layout.detail_item_subject_layout
            AppItemInfo.TYPE_PACKAGE -> R.layout.item_detail_package
            else -> R.layout.detail_label_layout
        }
        val view: View = inflater.inflate(layoutResId, parent, false)
        return AppDetailViewHolder(view)
    }

    override fun getItemCount(): Int = mDetailItemList?.size ?: 0

    override fun onBindViewHolder(holder: AppDetailViewHolder, position: Int) {
        val item = mDetailItemList!![position]
        holder.labelTv?.text = item.showLabel
        holder.subjectTv?.text = item.subject
        when (getItemViewType(position)) {
            AppItemInfo.TYPE_LABEL -> {
                holder.itemView.setOnClickListener {
                    mListener?.invoke(it, position)
                }
            }
            AppItemInfo.TYPE_PACKAGE -> {
                holder.itemView.findViewById<Button>(R.id.buttonPackage).setOnClickListener {
                    mListener?.invoke(it, position)
                }
            }
        }
        if (item.switchShowLabel.isNotBlank()) {
            holder.labelTv?.setTextIsSelectable(false)
            holder.labelTv?.setOnLongClickListener {
                val tag = it.getTag(R.id.key_switch) as Boolean?
                if (tag == null || tag == false) {
                    (it as TextView).text = item.switchShowLabel
                    it.setTag(R.id.key_switch, true)
                } else {
                    (it as TextView).text = item.showLabel
                    it.setTag(R.id.key_switch, false)
                }
                true
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        mDetailItemList?.let {
            return mDetailItemList!![position].type
        }
        return 0
    }


    /**
     * 设置点击item回调
     */
    fun setOnItemClickListener(listener: (view: View, position: Int) -> Unit) {
        mListener = listener
    }

    /**
     * 更新数据
     */
    fun setDetailList(detailList: List<AppItemInfo>) {
        mDetailItemList = detailList
    }

    fun getItemList(): List<AppItemInfo>? = mDetailItemList

    /**
     * 显示App详细信息的ViewHolder
     */
    class AppDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var subjectTv: TextView? = null
        var labelTv: TextView? = null

        init {
            subjectTv = view.findViewById(R.id.textViewSubject)
            labelTv = view.findViewById(R.id.textViewLabel)
        }
    }
}