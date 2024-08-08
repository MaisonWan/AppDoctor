package com.domker.doctor.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.domker.doctor.app.data.DataSorter
import com.domker.doctor.app.data.ListDataProcessor
import com.domker.doctor.util.log

/**
 * RecyclerView的适配器，可以使用数据排序，过滤等数据管理功能
 */
abstract class DataSortAdapter<VH : ViewHolder, T>(private val context: Context) :
    RecyclerView.Adapter<VH>(), DataSorter<T> {

    private val dataProcessor = ListDataProcessor<T>()
    private val sorterMap = mutableMapOf<Int, ((T) -> Comparable<*>?)>()

    // 排序类型，按照什么维度来排序
    protected var sorter: ((T) -> Comparable<*>?)? = null
    private var sortItemId: Int = 0

    // 升序或者降序
    protected var sortDesc = false

    // 每个单元点击之后
    private var itemClickListener: ((view: View, item: T) -> Unit)? = null

    init {
        // 设置排序结束之后的回调
        dataProcessor.setSortedCallback { ts ->
            onEndSort(ts, sortItemId, sortDesc)
        }

        // 设定具体排序的内容
        dataProcessor.setSortAction { mainData ->
            // 如果存在排序器，则使用
            sorter?.let {
                onSort(mainData, it, sortDesc)
            }
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        // 设置点击之后的操作
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(it, dataProcessor[position])
        }
    }

    /**
     * 初始化菜单ItemId和排序的映射关系，Key传递item id，或者使用MenuItemId
     * @return 是否识别到排序
     */
    fun initSorterMap(map: Map<Int, ((T) -> Comparable<*>?)>) {
        sorterMap.clear()
        sorterMap.putAll(map)
    }

    /**
     * 通过ItemId直接识别排序的类型
     * @return 是否未识别到，直接返回false
     */
    fun sortByItemId(itemId: Int): Boolean {
        return sorterMap[itemId]?.let {
            sortItemId = itemId
            onStartSort(it)
            dataProcessor.sort()
            true
        } ?: false
    }

    override fun onStartSort(comparable: (T) -> Comparable<*>?) {

    }

    override fun onSort(data: MutableList<T>, comparable: (T) -> Comparable<*>?, desc: Boolean) {

    }

    override fun onEndSort(data: List<T>, itemId: Int, desc: Boolean) {

    }

    /**
     * 更新数据
     */
    fun setData(data: List<T>) {
        dataProcessor.setData(data)
    }

    /**
     * 设置Item点击之后的监听器
     */
    fun setOnItemClickListener(listener: ((view: View, item: T) -> Unit)) {
        itemClickListener = listener
    }

    /**
     * 根据给定的数据，重新刷新展示
     */
    fun notifyAppList(appList: List<T>) {
        setData(appList)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyAllDataChanged() {
        log("notifyAllDataChanged ${dataProcessor.size()}")
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T {
        return dataProcessor[position]
    }

    override fun getItemCount(): Int = dataProcessor.size()

    fun getDataProcessor() = dataProcessor
}