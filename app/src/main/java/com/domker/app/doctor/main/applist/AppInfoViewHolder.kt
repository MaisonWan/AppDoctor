package com.domker.app.doctor.main.applist

import android.content.Context
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.app.doctor.R
import com.domker.app.doctor.db.AppEntity
import com.domker.app.doctor.util.IntentUtil
import com.domker.app.doctor.util.Router

/**
 * 显示App信息的ViewHolder
 */
class AppInfoViewHolder(private val context: Context, view: View) :
    RecyclerView.ViewHolder(view),
    View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
    var icon: ImageView? = null
    var appName: TextView? = null
    var appVersion: TextView? = null
    var packageName: TextView? = null
    var systemFlag: TextView? = null
    var currentAppEntity: AppEntity? = null

    init {
        icon = view.findViewById(R.id.imageViewIcon)
        appName = view.findViewById(R.id.textViewAppName)
        appVersion = view.findViewById(R.id.textViewAppVersion)
        packageName = view.findViewById(R.id.textViewPackageName)
        systemFlag = view.findViewById(R.id.imageViewSystemFlag)
        view.setOnCreateContextMenuListener(this)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        arrayOf(
            R.string.launch,
            R.string.apk_explorer,
            R.string.apk_settings
        ).forEachIndexed { index, i ->
            menu.add(0, i, index, i).setOnMenuItemClickListener(this)
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        if (currentAppEntity == null) {
            return false
        }
        val name = currentAppEntity!!.packageName
        return when (item.itemId) {
            R.string.launch -> {
                // 启动
                IntentUtil.createLaunchIntent(context, name).also {
                    context.startActivity(it)
                }
                true
            }
            R.string.apk_explorer -> {
                openPackageExplorer(currentAppEntity!!)
                true
            }
            R.string.apk_settings -> {
                context.startActivity(IntentUtil.createOpenSettingIntent(name))
                true
            }
            else -> false
        }
    }

    private fun openPackageExplorer(appEntity: AppEntity) {
        appEntity.sourceDir?.apply {
            ARouter.getInstance()
                .build(Router.EXPLORER_ACTIVITY)
                .withString("apk_source_path", this)
                .withString("package_name", appEntity.packageName)
                .navigation()
        }
    }
}