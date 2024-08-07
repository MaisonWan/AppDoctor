package com.domker.doctor.util

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.lang.ref.WeakReference

/**
 * Created by wanlipeng on 2018/3/5.
 */
class WeakHandler : Handler {

    private var mRef: WeakReference<IHandler>

    interface IHandler {
        fun handleMessage(msg: Message)
    }

    constructor(handler: IHandler) {
        mRef = WeakReference(handler)
    }

    constructor(looper: Looper, handler: IHandler) : super(looper) {
        mRef = WeakReference(handler)
    }

    override fun handleMessage(msg: Message) {
        mRef.get()?.handleMessage(msg)
    }
}
