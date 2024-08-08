package com.domker.doctor.tool

import com.domker.doctor.view.Link

/**
 * 工具链接
 */
class ToolLink(
    icon: Int,
    name: String,
    desc: String,
    val linkId: Int
) : Link(icon, name, desc)
