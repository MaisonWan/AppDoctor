package com.domker.doctor.tool.photo

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.domker.doctor.R
import com.domker.map.OpenExternalMapAppUtils

const val TYPE_LOCATION = 0x1
const val TYPE_NORMAL = 0x2

/**
 * Exif信息适配器
 * Created by wanlipeng on 2021/5/12 8:41 下午
 */
class ExifListAdapter(private val activity: Activity) :
    RecyclerView.Adapter<ExifListAdapter.ExifViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(activity)
    private var mDetailItemList: List<ExifItem>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExifViewHolder {
        val resId = if (viewType == TYPE_LOCATION) {
            R.layout.item_location
        } else {
            R.layout.item_subject_right
        }
        val view: View = inflater.inflate(resId, parent, false)
        return ExifViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExifViewHolder, position: Int) {
        val item = mDetailItemList!![position]
        when (item.type) {
            TYPE_LOCATION -> {
                bindLocation(item, holder)
            }
            else -> {
                holder.label?.text = item.content
                holder.subject?.text = item.subject
            }
        }
    }

    private fun bindLocation(
        item: ExifItem,
        holder: ExifViewHolder
    ) {
        val gps = item.expend as PhotoExif.GPS
        holder.subject?.text = item.subject
        holder.locationLat?.text = "纬度 ${gps.latitudeRef} ${gps.location.getLatitude()}"
        holder.locationLon?.text = "经度 ${gps.longitudeRef} ${gps.location.getLongitude()}"
        holder.buttonOpenMap?.setOnClickListener {
            OpenExternalMapAppUtils.openMapMarker(
                activity, gps.location.getLongitude().toString(),
                gps.location.getLatitude().toString(), "图片", "拍照位置", activity.packageName
            )
        }
    }

    override fun getItemCount(): Int {
        return mDetailItemList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        val item = mDetailItemList!![position]
        return item.type
    }

    fun setData(data: List<ExifItem>) {
        mDetailItemList = data
    }

    class ExifViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var subject: TextView? = view.findViewById(R.id.textViewSubject)
        var label: TextView? = view.findViewById(R.id.textViewContent)

        // TYPE_LOCATION
        var locationLat: TextView? = view.findViewById(R.id.textViewLocationLat)
        var locationLon: TextView? = view.findViewById(R.id.textViewLocationLon)
        val buttonOpenMap: Button? = view.findViewById(R.id.buttonOpenMap)
    }
}