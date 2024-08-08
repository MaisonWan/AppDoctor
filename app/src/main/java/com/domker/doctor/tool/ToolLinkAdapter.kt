package com.domker.doctor.tool

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.domker.doctor.view.Link
import com.domker.doctor.view.LinkAdapter

/**
 * 工具扩展模块的展示适配器
 */
class ToolLinkAdapter(context: Context) : LinkAdapter<ToolLink>(context) {

    init {
        setData(ToolLinkFactory().getData())
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        setOnItemClickListener { view, item ->
            navigate(view, item.linkId)
        }
    }

    private fun navigate(view: View, resId: Int) {
        Navigation.findNavController(view).navigate(resId, null)
    }
}