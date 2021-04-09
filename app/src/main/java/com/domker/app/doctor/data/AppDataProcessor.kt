package com.domker.app.doctor.data

const val SORT_NAME = 1
const val SORT_TIME = 2
const val SORT_SIZE = 3

/**
 * 数据处理器，主要处理排序，大小，过滤
 * Created by wanlipeng on 3/5/21 5:40 PM
 */
class AppDataProcessor(dataResource: List<AppEntity>) {
    private val mainData = mutableListOf<AppEntity>()
    private var sortedCallback: ((Int, Boolean) -> Unit)? = null

    var sortType = SORT_NAME
    var sortDesc = false

    init {
        resetData(dataResource)
    }

    fun size() = mainData.size

    fun data() = mainData

    operator fun get(index: Int): AppEntity {
        return mainData[index]
    }

    /**
     * 按照指定的类型排序
     * @param type 类型
     * @param desc 是否降序
     */
    fun sortBy(type: Int, desc: Boolean = false) {
        sortType = type
        sortDesc = desc

        if (desc) {
            when (type) {
                SORT_NAME -> mainData.sortByDescending { it.appName }
                SORT_SIZE -> mainData.sortByDescending { it.sourceApkSize }
                SORT_TIME -> mainData.sortByDescending { it.installTime }
            }
        } else {
            when (type) {
                SORT_NAME -> mainData.sortBy { it.appName }
                SORT_SIZE -> mainData.sortBy { it.sourceApkSize }
                SORT_TIME -> mainData.sortBy { it.installTime }
            }
        }
        sortedCallback?.let { it(sortType, sortDesc) }
    }

    /**
     * 按照指定的指标排序，如果是和目前相同的类型。则反转升序或者降序
     */
    fun sortBy(type: Int) {
        if (sortType == type) {
            sortDesc = sortDesc.not()
        } else {
            sortType = type
        }
        sortBy(type, sortDesc)
    }

    /**
     * 排序完的回调
     */
    fun setOnSortedCallback(callback: (Int, Boolean) -> Unit) {
        sortedCallback = callback
    }

    /**
     * 过滤器
     */
    fun filter(predicate: (AppEntity) -> Boolean) {
        mainData.filter { predicate(it) }
    }

    /**
     * 重置数据
     */
    fun resetData(dataResource: List<AppEntity>) {
        mainData.clear()
        mainData.addAll(dataResource)
        sortBy(sortType, sortDesc)
    }
}