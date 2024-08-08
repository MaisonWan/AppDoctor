package com.domker.doctor.app.applist

import android.content.Intent
import com.domker.doctor.view.Link

/**
 * 系统链接，点击可以直接跳转的链接。需要自己维护收集
 */
class SystemLink(
    iconRes: Int,
    name: String,
    val intent: Intent
) : Link(iconRes, name, "") {}
