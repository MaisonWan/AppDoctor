package com.domker.doctor.app.applist

import android.content.Context
import android.widget.Toast
import com.domker.doctor.view.LinkAdapter

/**
 * 系统链接的适配器
 */
class SystemLinkAdapter(private val context: Context) : LinkAdapter<SystemLink>(context) {

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        setOnItemClickListener { view, item ->
            try {
                context.startActivity(item.intent)
            } catch (e: java.lang.Exception) {
                Toast.makeText(context, "机型适配问题，打开${item.name}失败", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

}