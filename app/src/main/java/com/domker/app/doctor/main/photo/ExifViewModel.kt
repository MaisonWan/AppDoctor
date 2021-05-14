package com.domker.app.doctor.main.photo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *
 * Created by wanlipeng on 2021/5/12 8:59 下午
 */
class ExifViewModel : ViewModel() {

    val exif = MutableLiveData<PhotoExif>()
}