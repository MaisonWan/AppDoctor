package com.domker.base

/**
 * 系统版本和名字，api对应关系
 * Created by wanlipeng on 2020/6/4 1:48 PM
 */
object SystemVersion {
    data class Version(val name: String, val version: String, val api: Int)

    fun getVersion(api: Int): Version {
        return when(api) {
            30 -> Version("R", "11.0", api)
            29 -> Version("Q", "10.0", api)
            28 -> Version("Pie", "9.0", api)
            27 -> Version("Oreo", "8.1", api)
            26 -> Version("Oreo", "8.0", api)
            25 -> Version("Nougat", "7.1", api)
            24 -> Version("Nougat", "7.0", api)
            23 -> Version("Marshmallow", "6.0", api)
            22 -> Version("Lollipop", "5.1", api)
            21 -> Version("Lollipop", "5.0", api)
            20 -> Version("Kitkat Watch", "4.4W", api)
            19 -> Version("Kitkat", "4.4", api)
            18 -> Version("Jelly Bean", "4.3", api)
            17 -> Version("Jelly Bean", "4.2 - 4.2.2", api)
            16 -> Version("Jelly Bean", "4.1 - 4.1.1", api)
            15 -> Version("Ice Cream Sandwich", "4.0.3 - 4.0.4", api)
            14 -> Version("Ice Cream Sandwich", "4.0 - 4.0.2", api)
            13 -> Version("Honeycomb", "3.2", api)
            12 -> Version("Honeycomb", "3.1.x", api)
            11 -> Version("Honeycomb", "3.0.x", api)
            10 -> Version("Gingerbread", "2.3.3 - 2.3.4", api)
            9 -> Version("Gingerbread", "2.3 - 2.3.2", api)
            8 -> Version("Froyo", "2.2.x", api)
            7 -> Version("Eclair", "2.1.x", api)
            6 -> Version("Eclair", "2.0.1", api)
            5 -> Version("Eclair", "2.0", api)
            4 -> Version("Donut", "1.6", api)
            3 -> Version("Cupcake", "1.5", api)
            2 -> Version("Base", "1.1", api)
            1 -> Version("Base", "1.0", api)
            else -> Version("New Version", "Unknown", api)
        }
    }

    fun getShowLabel(v: Version): String {
        return "${v.name} (Android ${v.version}, API ${v.api})"
    }
}