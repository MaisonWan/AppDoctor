package com.domker.base.thread

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 线程池
 * Created by wanlipeng on 2019-11-28 18:13
 */
object AppExecutors {
    lateinit var executor: ExecutorService

    /**
     * 初始化
     */
    fun init() {
        executor = Executors.newFixedThreadPool(10)
    }

}