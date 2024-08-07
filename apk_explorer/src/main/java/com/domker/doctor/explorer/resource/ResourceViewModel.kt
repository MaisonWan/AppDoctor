package com.domker.doctor.explorer.resource

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thereisnospon.util.parse.type.ResFile

class ResourceViewModel : ViewModel() {

    private val _resFile = MutableLiveData<ResFile>()

    val resFile = _resFile
}