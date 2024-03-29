package com.domker.app.doctor.main.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R
import com.domker.app.doctor.data.AppCheckFactory
import com.domker.app.doctor.db.AppEntity

const val LAYOUT_TYPE_LIST = 0
const val LAYOUT_TYPE_GRID = 1

/**
 * 程序列表的适配器，主要展示每个应用图标和名称
 *
 * Created by wanlipeng on 2018/2/6.
 */
class AppListAdapter(private val context: Context, private val gridType: Int) :
    RecyclerView.Adapter<AppInfoViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var mListener: ((view: View, packageName: String) -> Unit)? = null
    private var mAppList: List<AppEntity>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppInfoViewHolder {
        val layoutResId = if (viewType == LAYOUT_TYPE_LIST) {
            R.layout.app_item_list_layout
        } else {
            R.layout.app_item_grid_layout
        }
        val view: View = inflater.inflate(layoutResId, parent, false)
        return AppInfoViewHolder(context, view)
    }

    override fun getItemCount(): Int = mAppList?.size ?: 0

    override fun onBindViewHolder(holder: AppInfoViewHolder, position: Int) {
        val item = mAppList?.get(position)
        item?.let {
            holder.currentAppEntity = it
            // 重新读取一下应用图标
            if (item.iconDrawable == null) {
                item.iconDrawable = AppCheckFactory.instance.getAppIcon(item.packageName)
            }
            item.iconDrawable?.apply {
                holder.icon?.setImageDrawable(this)
            }

            // 根据类型选展示的风格
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

    override fun getItemViewType(position: Int): Int {
        return gridType
    }

}