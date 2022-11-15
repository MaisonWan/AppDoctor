package com.domker.app.doctor.main.applist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.domker.app.doctor.R
import com.domker.app.doctor.data.AppCheckFactory
import com.domker.app.doctor.db.AppEntity
import com.domker.app.doctor.store.APP_LIST_STYLE_LIST
import com.domker.app.doctor.view.DataSortAdapter

const val LAYOUT_TYPE_LIST = 0
const val LAYOUT_TYPE_GRID = 1

/**
 * 程序列表的适配器，主要展示每个应用图标和名称
 *
 * Created by wanlipeng on 2018/2/6.
 */
class AppListAdapter(private val context: Context) :
    DataSortAdapter<AppInfoViewHolder, AppEntity>(context) {

    private var currentStyle: Int = LAYOUT_TYPE_LIST
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    init {
        // 默认按照名字排序
        sorter = AppEntity.sortByName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppInfoViewHolder {
        val layoutResId = if (viewType == LAYOUT_TYPE_LIST) {
            R.layout.app_item_list_layout
        } else {
            R.layout.app_item_grid_layout
        }
        val view: View = inflater.inflate(layoutResId, parent, false)
        return AppInfoViewHolder(context, view)
    }

    override fun onBindViewHolder(holder: AppInfoViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        holder.currentAppEntity = item

        bindIcon(item, holder)
        bindName(position, holder, item)
        // 包名
        holder.packageName?.text = item.packageName
        // 是否展示系统应用的标签
        holder.systemFlag?.visibility = if (item.isSystemApp) View.VISIBLE else View.GONE
    }

    private fun bindName(position: Int, holder: AppInfoViewHolder, item: AppEntity) {
        // 根据类型选展示的风格
        if (getItemViewType(position) == LAYOUT_TYPE_LIST) {
            holder.appName?.text = "${item.appName} (${item.versionName})"
        } else {
            holder.appVersion?.text = item.versionName
            holder.appName?.text = item.appName
        }
    }

    private fun bindIcon(item: AppEntity, holder: AppInfoViewHolder) {
        // 重新读取一下应用图标
        if (item.iconDrawable == null) {
            item.iconDrawable = AppCheckFactory.instance.getAppIcon(item.packageName)
        }
        item.iconDrawable?.apply {
            holder.icon?.setImageDrawable(this)
        }
    }

    /**
     * 按照指定的类型排序
     * @param comparable 排序的类型
     */
    override fun onStartSort(comparable: ((AppEntity) -> Comparable<*>?)) {
        super.onStartSort(comparable)
        if (sorter == comparable) {
            sortDesc = sortDesc.not()
        } else {
            sorter = comparable
        }
    }

    override fun onSort(
        data: MutableList<AppEntity>,
        comparable: (AppEntity) -> Comparable<*>?,
        desc: Boolean
    ) {
        super.onSort(data, comparable, desc)
        if (desc) {
            data.sortWith(compareByDescending(comparable))
        } else {
            data.sortWith(compareBy(comparable))
        }
    }

    override fun onEndSort(data: List<AppEntity>, itemId: Int, desc: Boolean) {
        super.onEndSort(data, itemId, desc)
        notifyAllDataChanged()
    }

    /**
     * 首次加载的时候，需要设定完毕之后重新布局
     */
    fun notifyAppListStyleChanged(style: String) {
        setCurrentAppListStyle(style)
        notifyAllDataChanged()
    }

    /**
     * 设置当前俩列表的格式
     */
    private fun setCurrentAppListStyle(style: String) {
        currentStyle = if (style == APP_LIST_STYLE_LIST) {
            LAYOUT_TYPE_LIST
        } else {
            LAYOUT_TYPE_GRID
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentStyle
    }

}