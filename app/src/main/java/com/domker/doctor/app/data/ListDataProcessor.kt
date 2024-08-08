package com.domker.doctor.app.data

/**
 * 数据管理存储，排序等操作
 *
 */
class ListDataProcessor<T> : DataProcessor<T> {
    private val mutex = Object()

    // 存储数据
    private val listData = mutableListOf<T>()

    // 排序函数
    private var sortAction: ((MutableList<T>) -> Unit)? = null

    // 排序之后的回调
    private var sortedCallback: ((List<T>) -> Unit)? = null

    override fun size(): Int {
        synchronized(mutex) {
            return listData.size
        }
    }

    override fun data(): List<T> = listData

    override fun cloneData(): List<T> {
        return ArrayList<T>(listData)
    }

    override fun setData(list: List<T>) {
        synchronized(mutex) {
            listData.clear()
            listData.addAll(list)
        }
        sort()
    }

    /**
     * 设置排序器
     */
    fun setSortAction(action: (MutableList<T>) -> Unit) {
        sortAction = action
    }

    /**
     * 数据排序之后的回调
     */
    fun setSortedCallback(callback: (List<T>) -> Unit) {
        sortedCallback = callback
    }

    /**
     * 执行排序操作
     */
    override fun sort() {
        sortAction?.let { it(listData) }
        sortedCallback?.invoke(listData)
    }

    override fun filter(predicate: (T) -> Boolean) {
        listData.filter { predicate(it) }
    }

    override fun get(index: Int): T = listData[index]

}