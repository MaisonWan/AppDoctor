package com.domker.doctor.app.detail.container

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.doctor.R
import com.domker.doctor.app.detail.DetailItemViewHolder
import com.domker.doctor.db.AppEntity
import com.domker.doctor.entiy.AppItemInfo
import com.domker.doctor.util.Router

/**
 * 创建包路径查看的容器
 */
class PackageContainer(inflater: LayoutInflater) :
    AbstractContainer<DetailItemViewHolder>(inflater, DETAIL_TYPE_PACKAGE) {

    override fun createContainerView(parent: ViewGroup): View {
        return createView(R.layout.item_detail_package, parent)
    }

    override fun bindViewHolder(
        holder: DetailItemViewHolder,
        data: AppItemInfo,
        position: Int
    ) {
        holder.content?.text = data.content
        holder.subject?.text = data.subject

        val button = holder.itemView.findViewById<Button>(R.id.buttonPackage)
        button.setOnClickListener {
            data.appEntity?.also { e ->
                openPackageExplorer(e)
            }
        }
    }

    private fun openPackageExplorer(appEntity: AppEntity) {
        appEntity.sourceDir?.let {
            ARouter.getInstance()
                .build(Router.EXPLORER_ACTIVITY)
                .withString("apk_source_path", it)
                .withString("package_name", appEntity.packageName)
                .navigation()
        }
    }
}