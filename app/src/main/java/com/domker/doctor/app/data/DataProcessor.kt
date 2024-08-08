package com.domker.doctor.app.data

/**
 * 数据处理器接口
 * Created by wanlipeng on 3/5/21 6:00 PM
 */
interface DataProcessor<T> {

    /**
     * 数据数量
     */
    fun size(): Int

    /**
     * 直接返回数据集合
     */
    fun data(): List<T>

    /**
     * 内存拷贝一份新的，然后返回
     */
    fun cloneData(): List<T>

    /**
     * 设置数据，内部先清空，然后添加到独立数据结构中，不复用参数
     */
    fun setData(list: List<T>)

    /**
     * 按照给定函数做排序动作
     */
    fun sort()

    /**
     * 过滤器，过滤元素
     */
    fun filter(predicate: (T) -> Boolean)

    /**
     * 通过下标索引，返回指定数据
     */
    operator fun get(index: Int): T

}