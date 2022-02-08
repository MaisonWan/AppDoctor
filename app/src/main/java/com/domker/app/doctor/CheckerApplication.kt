package com.domker.app.doctor

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.app.doctor.data.AppDatabase
import com.domker.app.doctor.view.TypeFacePool
import com.domker.base.thread.AppExecutors

/**
 * 应用Application
 * Created by wanlipeng on 2019-11-01 16:19
 */
class CheckerApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        CheckerApplication.applicationContext = this
        AppExecutors.init()

        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (BuildConfig.DEBUG) {
            // 打印日志
            ARouter.openLog()
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug()
        }
        ARouter.init(this)
        TypeFacePool.init(this)

        appDatabase = Room.databaseBuilder(this, AppDatabase::class.java, "app_list.db")
            .allowMainThreadQueries()
            .setQueryExecutor(AppExecutors.executor)
            .build()
    }

    companion object {
        lateinit var appDatabase: AppDatabase
        lateinit var applicationContext: Context
    }
}