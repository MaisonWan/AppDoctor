package com.domker.app.doctor.main.photo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R
import com.domker.app.doctor.entiy.AppItemInfo

/**
 * Exif信息适配器
 * Created by wanlipeng on 2021/5/12 8:41 下午
 */
class ExifListAdapter(context: Context) : RecyclerView.Adapter<ExifListAdapter.ExifViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var mDetailItemList: List<AppItemInfo>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExifViewHolder {
        val view: View = inflater.inflate(R.layout.item_subject_right, parent, false)
        return ExifViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExifViewHolder, position: Int) {
        val item = mDetailItemList!![position]
        holder.label?.text = item.showLabel
        holder.subject?.text = item.subject
    }

    override fun getItemCount(): Int {
        return mDetailItemList?.size ?: 0
    }

    fun setData(data: List<AppItemInfo>) {
        mDetailItemList = data
    }

    class ExifViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var subject: TextView? = null
        var label: TextView? = null

        init {
            subject = view.findViewById(R.id.textViewSubject)
            label = view.findViewById(R.id.textViewLabel)
        }
    }
}