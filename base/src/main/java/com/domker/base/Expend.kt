package com.domker.base

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * Created by wanlipeng on 2020/6/3 4:42 PM
 */

fun Boolean.toChinese(): String {
    return if (this) "是" else "否"
}

fun String?.toChinese(): String {
    return if (this.isNullOrBlank()) "无" else this
}

/**
 * 对RecyclerView扩展分割线
 */
fun RecyclerView.addItemDecoration(context: Context, @DrawableRes drawableId: Int) {
    val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    ResourcesCompat.getDrawable(resources, drawableId, context.theme)?.apply {
        itemDecoration.setDrawable(this)
    }
    this.addItemDecoration(itemDecoration)
}

/**
 * 启动Activity
 */
fun Intent.startActivity(context: Context) {
    context.startActivity(this)
}