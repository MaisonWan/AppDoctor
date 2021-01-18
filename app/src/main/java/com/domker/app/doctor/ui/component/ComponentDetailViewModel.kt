package com.domker.app.doctor.ui.component

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 组件详情
 * Created by wanlipeng on 2020/6/17 6:28 PM
 */
class ComponentDetailViewModel : ViewModel() {

    val detail: MutableLiveData<ComponentInfo> = MutableLiveData()
}