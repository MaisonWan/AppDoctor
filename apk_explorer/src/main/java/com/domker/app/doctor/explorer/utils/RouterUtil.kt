package com.domker.app.doctor.explorer.utils

import com.alibaba.android.arouter.launcher.ARouter
import java.io.File

/**
 * 跳转路由的工具类
 * Created by wanlipeng on 12/31/20 5:42 PM
 */
object RouterUtil {

    /**
     * 打开资源浏览界面
     */
    fun openResourceExplorerActivity(resFilePath: String) {
        ARouter.getInstance()
                .build("/resource_explorer/activity")
                .withString("resource_file_path", resFilePath)
                .navigation()
    }

    /**
     * 打开JsonViewer的界面
     */
    fun openJsonViewerActivity(file: File) {
        ARouter.getInstance()
                .build("/json_viewer/activity")
                .withString("json_file_path", file.absolutePath)
                .navigation()
    }
}