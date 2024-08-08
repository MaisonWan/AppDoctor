package com.domker.doctor.app.data

/**
 * 排序的三个动作
 */
interface DataSorter<T> {

    fun onStartSort(comparable: ((T) -> Comparable<*>?))

    /**
     * 具体的排序操作
     */
    fun onSort(data: MutableList<T>, comparable: ((T) -> Comparable<*>?), desc: Boolean)

    /**
     * 当数据重新排序之后，要重新做展示
     */
    fun onEndSort(data: List<T>, itemId: Int, desc: Boolean)
}