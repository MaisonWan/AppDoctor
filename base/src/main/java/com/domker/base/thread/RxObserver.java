package com.domker.base.thread;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 数据返回统一处理
 *
 * @param <T>
 */
public abstract class RxObserver<T> implements Observer<T> {
    private static final String TAG = RxObserver.class.getSimpleName();

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    public abstract void onSuccess(T t);

}
