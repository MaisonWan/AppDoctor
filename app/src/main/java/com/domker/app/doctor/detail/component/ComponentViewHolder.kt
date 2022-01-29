package com.domker.app.doctor.detail.component

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R

/**
 * 组件展示Item的ViewHolder
 */
class ComponentViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    /**
     * 标题
     */
    var subject: TextView? = view.findViewById(R.id.textViewSubject)

    /**
     * 下面的内容
     */
    var content: TextView? = view.findViewById(R.id.textViewFile)

    /**
     * 右侧的指示图标，或者文件类型
     */
    var icon: ImageView? = view.findViewById(R.id.imageViewType)

    init {
        subject?.setTextIsSelectable(false)
        content?.setTextIsSelectable(false)
    }
}