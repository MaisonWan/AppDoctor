package com.domker.app.doctor.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R
import com.domker.app.doctor.data.AppCheckFactory
import com.domker.app.doctor.data.AppEntity

const val LAYOUT_TYPE_LIST = 0
const val LAYOUT_TYPE_GRID = 1

/**
 * 程序列表的适配器
 *
 * Created by wanlipeng on 2018/2/6.
 */
class AppListAdapter(private val context: Context, private val gridType: Int) :
        RecyclerView.Adapter<AppListAdapter.AppInfoViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var mListener: ((view: View, packageName: String) -> Unit)? = null
    private var mAppList: List<AppEntity>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppInfoViewHolder {
        val layoutResId = if (viewType == LAYOUT_TYPE_LIST) {
            R.layout.app_item_list_layout
        } else {
            R.layout.app_item_grid_layout
        }
        val view: View = inflater.inflate(layoutResId, null)
        return AppInfoViewHolder(view)
    }

    override fun getItemCount(): Int = mAppList?.size ?: 0

    override fun onBindViewHolder(holder: AppInfoViewHolder, position: Int) {
        val item = mAppList?.get(position)
        item?.let {
            if (item.iconDrawable == null) {
                item.iconDrawable = AppCheckFactory.getInstance(context).getAppIcon(item.packageName)
            }
            item.iconDrawable?.apply {
                holder.icon?.setImageDrawable(this)
            }
            if (gridType == LAYOUT_TYPE_LIST) {
                holder.appName?.text = "${item.appName} (${item.versionName})"
            } else {
                holder.appVersion?.text = item.versionName
                holder.appName?.text = item.appName
            }
            holder.packageName?.text = item.packageName
            holder.systemFlag?.visibility = if (item.isSystemApp) View.VISIBLE else View.GONE
            holder.itemView.setOnClickListener { v ->
                mListener?.invoke(v, it.packageName)
            }
        }
    }

    /**
     * 设置点击item回调
     */
    fun setOnItemClickListener(listener: (view: View, packageName: String) -> Unit) {
        mListener = listener
    }

    /**
     * 更新数据
     */
    fun setAppList(appList: List<AppEntity>) {
        mAppList = appList
    }

    /**
     * 获取当前的列表
     */
    fun getAppList() = mAppList

    fun getItem(position: Int): AppEntity? {
        return mAppList?.get(position)
    }

    /**
     * 显示App信息的ViewHolder
     */
    class AppInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var icon: ImageView? = null
        var appName: TextView? = null
        var appVersion: TextView? = null
        var packageName: TextView? = null
        var systemFlag: TextView? = null

        init {
            icon = view.findViewById(R.id.imageViewIcon)
            appName = view.findViewById(R.id.textViewAppName)
            appVersion = view.findViewById(R.id.textViewAppVersion)
            packageName = view.findViewById(R.id.textViewPackageName)
            systemFlag = view.findViewById(R.id.imageViewSystemFlag)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return gridType
    }
}