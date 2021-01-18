package com.domker.app.doctor.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.domker.app.doctor.data.AppEntity

class HomeViewModel : ViewModel() {

    private val _appInfo = MutableLiveData<AppEntity>()

    val appInfo: MutableLiveData<AppEntity> = _appInfo
}