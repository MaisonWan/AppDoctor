package com.domker.doctor.main.applist

import android.content.Intent

/**
 * 系统链接，点击可以直接跳转的链接。需要自己维护收集
 */
data class SystemLink(
    val name: String,
    val iconRes: Int,
    val intent: Intent
)
