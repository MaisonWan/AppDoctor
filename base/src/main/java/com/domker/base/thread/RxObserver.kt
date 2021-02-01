package com.domker.base.thread

import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

/**
 * 数据返回统一处理
 *
 * @param <T>
</T> */
abstract class RxObserver<T> : Observer<T> {
    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

    override fun onComplete() {}
    override fun onSubscribe(d: Disposable) {}
    abstract fun onSuccess(t: T)

    companion object {
        private val TAG = RxObserver::class.java.simpleName
    }
}