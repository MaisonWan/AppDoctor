package com.domker.app.doctor.data

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.domker.app.doctor.main.AppViewModel

/**
 * 全局状态
 * Created by wanlipeng on 3/5/21 2:25 PM
 */
object AppState {

    lateinit var appViewModel: AppViewModel

    /**
     * 初始化
     */
    fun init(owner: ViewModelStoreOwner) {
        appViewModel = ViewModelProvider(owner).get(AppViewModel::class.java)
    }
}
