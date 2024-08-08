package com.domker.doctor

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.doctor.base.thread.AppExecutors
import com.domker.doctor.data.db.AppDatabase
import com.domker.doctor.util.log
import com.domker.doctor.view.TypeFacePool

/**
 * 应用Application
 * Created by wanlipeng on 2019-11-01 16:19
 */
class CheckerApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        com.domker.doctor.CheckerApplication.Companion.applicationContext = this

        AppExecutors.init()
        log("create db begin")
        com.domker.doctor.CheckerApplication.Companion.appDatabase =
            Room.databaseBuilder(this, AppDatabase::class.java, "app_list.db")
                .allowMainThreadQueries()
                .setQueryExecutor(AppExecutors.executor)
                .build()
        log("create db end")
    }

    override fun onCreate() {
        super.onCreate()
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (BuildConfig.DEBUG) {
            // 打印日志
            ARouter.openLog()
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug()
        }
        ARouter.init(this)
        TypeFacePool.init(this)
    }

    companion object {
        lateinit var appDatabase: AppDatabase
        lateinit var applicationContext: Context
    }
}