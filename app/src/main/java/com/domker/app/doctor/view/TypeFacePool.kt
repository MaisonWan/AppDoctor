package com.domker.app.doctor.view

import android.content.Context
import android.graphics.Typeface

/**
 *
 * Created by wanlipeng on 4/10/21 11:42 PM
 */
object TypeFacePool {
    lateinit var openSansLight: Typeface

    fun init(context: Context) {
        openSansLight = Typeface.createFromAsset(context.assets, "OpenSans-Light.ttf")
    }
}