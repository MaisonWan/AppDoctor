package com.domker.base.thread

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.FlowableTransformer
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * RxJava中一些封装
 *
 *
 * Created by wanlipeng on 2019/2/9 11:57 PM
 */
object RxHelper {

    fun <T : Any> observableIO2Main(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T : Any> flowableIO2Main(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T : Any> singleIO2Main(): SingleTransformer<T, T> {
        return SingleTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    //
    //    private static <T> ObservableSource<T> composeContext(Context context, Observable<T> observable) {
    //        if (context instanceof RxActivity) {
    //            return observable.compose(((RxActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
    //        } else if (context instanceof RxFragmentActivity) {
    //            return observable.compose(((RxFragmentActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
    //        } else if (context instanceof RxAppCompatActivity) {
    //            return observable.compose(((RxAppCompatActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
    //        } else {
    //            return observable;
    //        }
    //    }
}