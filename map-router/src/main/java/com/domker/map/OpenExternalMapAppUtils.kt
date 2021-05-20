package com.domker.map

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.domker.map.popwindow.SelectPopupWindow
import com.domker.map.popwindow.SelectPopupWindow.OnPopWindowClickListener
import com.domker.map.web.TbWebViewActivity
import java.util.*

const val TYPE_MAPVIEW_WITH_TIPS = 0
const val TYPE_MAPVIEW_DIRECTION = 1
const val TYPE_MAPVIEW_NAVI = 2


/**
 * Created by cretin on 2017/5/4.
 */
object OpenExternalMapAppUtils {
    private val SUPPORT_MAP_APP = arrayOf(
        //高德
        "com.autonavi.minimap",

        //百度
        "com.baidu.BaiduMap"
    )

    private var menuWindow: SelectPopupWindow? = null

    /**
     * 打开地图显示位置，选择已经安装的地图
     *
     * @param activity
     * @param longitude
     * @param latitude
     * @param title
     * @param content
     * @param appName
     */
    fun openMapMarker(
        activity: Activity, longitude: String, latitude: String,
        title: String, content: String, appName: String
    ) {
        val mapApps = getMapApps(activity)
        if (!mapApps.isEmpty()) {
            //有安装客户端 打开PopWindow显示数据
            if (mapApps.contains(SUPPORT_MAP_APP[0]) && mapApps.contains(
                    SUPPORT_MAP_APP[1]
                )
            ) {
                showAlertDialog(
                    activity, TYPE_MAPVIEW_WITH_TIPS, true,
                    true, longitude, latitude, title, "", "", "", content, appName
                )
            } else if (mapApps.contains(SUPPORT_MAP_APP[0])) {
                showAlertDialog(
                    activity, TYPE_MAPVIEW_WITH_TIPS, true, false,
                    longitude, latitude, title, "", "", "", content, appName
                )
            } else if (mapApps.contains(SUPPORT_MAP_APP[1])) {
                showAlertDialog(
                    activity, TYPE_MAPVIEW_WITH_TIPS, false, true,
                    longitude, latitude, title, "", "", "", content, appName
                )
            }
        } else {
            //没有安装客户端 打开网页版
            openBrowserMarkerMap(activity, longitude, latitude, appName, title, content, false)
        }
    }

    //打开地图显示位置
    fun openMapMarker(
        activity: Activity, longitude: String, latitude: String,
        title: String, content: String, appName: String, useOutWeb: Boolean
    ) {
        val mapApps = getMapApps(activity)
        if (!mapApps.isEmpty()) {
            //有安装客户端 打开PopWindow显示数据
            if (mapApps.contains(SUPPORT_MAP_APP[0]) && mapApps.contains(
                    SUPPORT_MAP_APP[1]
                )
            ) {
                showAlertDialog(
                    activity, TYPE_MAPVIEW_WITH_TIPS, true,
                    true, longitude, latitude, title, "", "", "", content, appName
                )
            } else if (mapApps.contains(SUPPORT_MAP_APP[0])) {
                showAlertDialog(
                    activity, TYPE_MAPVIEW_WITH_TIPS, true, false,
                    longitude, latitude, title, "", "", "", content, appName
                )
            } else if (mapApps.contains(SUPPORT_MAP_APP[1])) {
                showAlertDialog(
                    activity, TYPE_MAPVIEW_WITH_TIPS, false, true,
                    longitude, latitude, title, "", "", "", content, appName
                )
            }
        } else {
            //没有安装客户端 打开网页版
            openBrowserMarkerMap(activity, longitude, latitude, appName, title, content, useOutWeb)
        }
    }

    /**
     * 打开地图显示位置
     *
     * @param activity
     * @param longitude
     * @param latitude
     * @param title
     * @param content
     * @param appName
     * @param useOutWeb
     * @param forceUseBro 强制使用浏览器打开 不考虑是否有app
     */
    fun openMapMarker(
        activity: Activity, longitude: String, latitude: String,
        title: String, content: String, appName: String, useOutWeb: Boolean, forceUseBro: Boolean
    ) {
        if (forceUseBro) {
            openBrowserMarkerMap(activity, longitude, latitude, appName, title, content, useOutWeb)
        } else {
            val mapApps = getMapApps(activity)
            if (!mapApps.isEmpty()) {
                //有安装客户端 打开PopWindow显示数据
                if (mapApps.contains(SUPPORT_MAP_APP[0]) && mapApps.contains(
                        SUPPORT_MAP_APP[1]
                    )
                ) {
                    showAlertDialog(
                        activity, TYPE_MAPVIEW_WITH_TIPS, true,
                        true, longitude, latitude, title, "", "", "", content, appName
                    )
                } else if (mapApps.contains(SUPPORT_MAP_APP[0])) {
                    showAlertDialog(
                        activity, TYPE_MAPVIEW_WITH_TIPS, true, false,
                        longitude, latitude, title, "", "", "", content, appName
                    )
                } else if (mapApps.contains(SUPPORT_MAP_APP[1])) {
                    showAlertDialog(
                        activity, TYPE_MAPVIEW_WITH_TIPS, false, true,
                        longitude, latitude, title, "", "", "", content, appName
                    )
                }
            } else {
                //没有安装客户端 打开网页版
                openBrowserMarkerMap(
                    activity,
                    longitude,
                    latitude,
                    appName,
                    title,
                    content,
                    useOutWeb
                )
            }
        }
    }

    /**
     * 打开地图显示路径规划 网页版没有路径规划
     *
     * @param activity
     * @param sLongitude
     * @param sLatitude
     * @param sName
     * @param dLongitude
     * @param dLatitude
     * @param dName
     * @param appName
     */
    fun openMapDirection(
        activity: Activity, sLongitude: String, sLatitude: String,
        sName: String, dLongitude: String, dLatitude: String,
        dName: String, appName: String
    ) {
        val mapApps = getMapApps(activity)
        if (!mapApps.isEmpty()) {
            //有安装客户端 打开PopWindow显示数据
            if (mapApps.contains(SUPPORT_MAP_APP[0]) && mapApps.contains(
                    SUPPORT_MAP_APP[1]
                )
            ) {
                showAlertDialog(
                    activity, TYPE_MAPVIEW_DIRECTION, true, true,
                    sLongitude, sLatitude, sName, dLongitude, dLatitude, dName, "", appName
                )
            } else if (mapApps.contains(SUPPORT_MAP_APP[0])) {
                showAlertDialog(
                    activity, TYPE_MAPVIEW_DIRECTION, true, false,
                    sLongitude, sLatitude, sName, dLongitude, dLatitude, dName, "", appName
                )
            } else if (mapApps.contains(SUPPORT_MAP_APP[1])) {
                showAlertDialog(
                    activity, TYPE_MAPVIEW_DIRECTION, false, true,
                    sLongitude, sLatitude, sName, dLongitude, dLatitude, dName, "", appName
                )
            }
        } else {
            //没有安装客户端 但是又没有网页版的 提示
            Toast.makeText(activity, "请下载百度或高德地图客户端", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 打开地图导航 此方法只有目标点 用于调起客户端执行导航 若需要调起网页版 请调用此方法
     * [.openMapNaviWithTwoPoints]
     */
    fun openMapNavi(
        activity: Activity, longitude: String, latitude: String,
        title: String, content: String, appName: String
    ) {
        val mapApps = getMapApps(activity)
        if (!mapApps.isEmpty()) {
            //有安装客户端 打开PopWindow显示数据
            if (mapApps.contains(SUPPORT_MAP_APP[0]) && mapApps.contains(
                    SUPPORT_MAP_APP[1]
                )
            ) {
                showAlertDialog(
                    activity, TYPE_MAPVIEW_NAVI, true, true,
                    longitude, latitude, title, "", "", "", content, appName
                )
            } else if (mapApps.contains(SUPPORT_MAP_APP[0])) {
                showAlertDialog(
                    activity, TYPE_MAPVIEW_NAVI, true, false,
                    longitude, latitude, title, "", "", "", content, appName
                )
            } else if (mapApps.contains(SUPPORT_MAP_APP[1])) {
                showAlertDialog(
                    activity, TYPE_MAPVIEW_NAVI, false, true,
                    longitude, latitude, title, "", "", "", content, appName
                )
            }
        } else {
            //没有安装客户端 但是又没有网页版的 提示
            Toast.makeText(activity, "请下载百度或高德地图客户端", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 打开地图导航 如果需要调用网页版导航 需要提供两个点 只提供一个点的请调用
     * [.openMapNavi]
     */
    fun openMapNaviWithTwoPoints(
        activity: Activity,
        sLongitude: String,
        sLatitude: String,
        sName: String,
        dLongitude: String,
        dLatitude: String,
        dName: String,
        region: String,
        appName: String
    ) {
        //百度地图网页版导航必须提供两个坐标点
        openBrowserNaviMap(
            activity,
            sLongitude,
            sLatitude,
            sName,
            dLongitude,
            dLatitude,
            dName,
            region,
            appName,
            false
        )
    }

    /**
     * 打开地图导航 如果需要调用网页版导航 需要提供两个点 只提供一个点的请调用 设置是否需要调用外部浏览器打开
     * [.openMapNavi]
     */
    fun openMapNaviWithTwoPoints(
        activity: Activity,
        sLongitude: String,
        sLatitude: String,
        sName: String,
        dLongitude: String,
        dLatitude: String,
        dName: String,
        region: String,
        appName: String,
        useOutWeb: Boolean
    ) {
        //百度地图网页版导航必须提供两个坐标点
        openBrowserNaviMap(
            activity,
            sLongitude,
            sLatitude,
            sName,
            dLongitude,
            dLatitude,
            dName,
            region,
            appName,
            useOutWeb
        )
    }
    //************************************************************************
    //*************************    百度专区     *******************************
    //************************************************************************
    /**
     * 调起百度客户端 自定义打点
     * lat,lng (先纬度，后经度)
     * 40.057406655722,116.2964407172
     *
     * @param activity
     * @param longitude
     * @param latitude
     * @param title
     * @param content
     */
    fun openBaiduMarkerMap(
        activity: Context, longitude: String, latitude: String,
        title: String, content: String
    ) {
        val intent = Intent(
            "android.intent.action.VIEW",
            Uri.parse(
                "baidumap://map/marker?location=" + latitude + "," +
                        longitude + "&title=" + title + "&content=" + content + "&traffic=on"
            )
        )
        activity.startActivity(intent)
    }

    /**
     * 调起百度客户端 展示地图
     * lat,lng (先纬度，后经度)
     * 40.057406655722,116.2964407172
     * 范围参数
     * lat,lng,lat,lng (先纬度，后经度, 先左下,后右上)
     *
     * @param activity
     */
    fun openBaiduCenterMap(
        activity: Context,
        cLongitude: String,
        cLatitude: String,
        zoom: String,
        traffic: Boolean,
        lLatitude: String,
        lLongitude: String,
        rLatitude: String,
        rLongitude: String
    ) {
        val intent = Intent(
            "android.intent.action.VIEW",
            Uri.parse(
                "baidumap://map/show?center=" + cLatitude + "," +
                        cLongitude + "&zoom=" + zoom + "&traffic=" + (if (traffic) "on" else "off") +
                        "&bounds=" + lLatitude + "," + lLongitude + "," + rLatitude + "," + rLongitude
            )
        )
        activity.startActivity(intent)
    }

    /**
     * 调起百度客户端 驾车导航
     * lat,lng (先纬度，后经度)
     * 40.057406655722,116.2964407172
     *
     * @param activity
     */
    fun openBaiduNaviMap(activity: Context, longitude: String, latitude: String) {
        val intent = Intent(
            "android.intent.action.VIEW",
            Uri.parse("baidumap://map/navi?location=$latitude,$longitude")
        )
        activity.startActivity(intent)
    }

    /**
     * 调起百度客户端 路径规划
     * lat,lng (先纬度，后经度)
     * 40.057406655722,116.2964407172
     * lat,lng,lat,lng (先纬度，后经度, 先左下,后右上)
     *
     * @param activity
     */
    fun openBaiduDirectionMap(
        activity: Context, sLongitude: String, sLatitude: String, sName: String,
        dLongitude: String, dLatitude: String, dName: String
    ) {
        val intent = Intent(
            "android.intent.action.VIEW",
            Uri.parse(
                "baidumap://map/direction?origin=name:" +
                        sName + "|latlng:" + sLatitude + "," + sLongitude + "&destination=name:" +
                        dName + "|latlng:" + dLatitude + "," + dLongitude + "&" +
                        "mode=transit&sy=0&index=0&target=0"
            )
        )
        activity.startActivity(intent)
    }
    //************************************************************************
    //*************************    高德专区     *******************************
    //************************************************************************
    /**
     * 调起高德客户端 展示标注点
     * lat,lng (先纬度，后经度)
     * 40.057406655722,116.2964407172
     * 根据名称或经纬度，启动高德地图产品展示一个标注点，如分享位置，标注店铺。支持版本V4.1.3起。
     *
     * @param activity
     * @param longitude
     * @param latitude
     * @param appName
     * @param poiname
     */
    fun openGaodeMarkerMap(
        activity: Context, longitude: String, latitude: String,
        appName: String, poiname: String
    ) {
        val intent = Intent(
            "android.intent.action.VIEW",
            Uri.parse(
                "androidamap://viewMap?sourceApplication=" +
                        appName + "&poiname=" + poiname + "&lat=" + latitude + "&lon=" + longitude + "&dev=1"
            )
        )
        intent.setPackage("com.autonavi.minimap")
        activity.startActivity(intent)
    }

    /**
     * 调起高德客户端 路径规划
     * lat,lng (先纬度，后经度)
     * 40.057406655722,116.2964407172
     * 输入起点和终点，搜索公交、驾车或步行的线路。支持版本 V4.2.1 起。
     *
     * @param activity
     * @param sLongitude
     * @param sLatitude
     * @param sName
     * @param dLongitude
     * @param dLatitude
     * @param dName
     * @param appName
     */
    fun openGaodeRouteMap(
        activity: Context, sLongitude: String, sLatitude: String, sName: String,
        dLongitude: String, dLatitude: String, dName: String, appName: String
    ) {
        val intent = Intent(
            "android.intent.action.VIEW",
            Uri.parse(
                "amapuri://route/plan/?sourceApplication=" + appName +
                        "&sid=&slat=" + sLatitude + "&slon=" +
                        sLongitude + "&sname=" + sName + "&did=&dlat=" +
                        dLatitude + "&dlon=" + dLongitude + "&dname=" + dName + "&dev=1&t=1"
            )
        )
        intent.setPackage("com.autonavi.minimap")
        activity.startActivity(intent)
    }

    /**
     * 调起高德客户端 我的位置
     * 显示我当前的位置。支持版本V4.2.1 起。
     *
     * @param activity
     * @param appName
     */
    fun openGaodeMyLocationMap(activity: Context, appName: String) {
        val intent = Intent(
            "android.intent.action.VIEW",
            Uri.parse("androidamap://myLocation?sourceApplication=$appName")
        )
        intent.setPackage("com.autonavi.minimap")
        activity.startActivity(intent)
    }

    /**
     * 调起高德客户端 导航
     * 输入终点，以用户当前位置为起点开始路线导航，提示用户每段行驶路线以到达目的地。支持版本V4.1.3 起。
     * lat,lng (先纬度，后经度)
     * 40.057406655722,116.2964407172
     *
     * @param activity
     * @param appName
     */
    fun openGaodeNaviMap(
        activity: Context, appName: String, poiname: String,
        latitude: String, longitude: String
    ) {
        val intent = Intent(
            "android.intent.action.VIEW",
            Uri.parse(
                "androidamap://navi?sourceApplication=" + appName + "&poiname=" +
                        poiname + "&lat=" + latitude + "&lon=" + longitude + "&dev=1&style=2"
            )
        )
        intent.setPackage("com.autonavi.minimap")
        activity.startActivity(intent)
    }

    /**
     * 调起高德客户端 公交线路查询
     * 输入公交线路名称，如 445，搜索该条线路经过的所有公交站点。支持版本 v4.2.1 起。
     *
     * @param activity
     * @param appName
     */
    fun openGaodeBusMap(
        activity: Context, appName: String, busName: String,
        city: String
    ) {
        val intent = Intent(
            "android.intent.action.VIEW",
            Uri.parse("androidamap://bus?sourceApplication=$appName&busname=$busName&city=$city")
        )
        intent.setPackage("com.autonavi.minimap")
        activity.startActivity(intent)
    }

    /**
     * 调起高德客户端 地图主图
     * 进入高德地图主图页面。支持版本V4.2.1起。
     *
     * @param activity
     * @param appName
     */
    fun openGaodeRootmapMap(activity: Context, appName: String) {
        val intent = Intent(
            "android.intent.action.VIEW",
            Uri.parse("androidamap://rootmap?sourceApplication=$appName")
        )
        intent.setPackage("com.autonavi.minimap")
        activity.startActivity(intent)
    }
    //************************************************************************
    //*************************    网页专区     *******************************
    //************************************************************************
    /**
     * 打开网页版 反向地址解析
     *
     * @param activity
     * @param longitude
     * @param latitude
     * @param appName
     * @param useOutWeb 是否调用外部的浏览器 默认不调用
     */
    fun openBrowserMarkerMap(
        activity: Context, longitude: String, latitude: String,
        appName: String, title: String, content: String, useOutWeb: Boolean
    ) {
        if (useOutWeb) {
            val mapUri = Uri.parse(
                "http://api.map.baidu.com/marker?location=" + latitude + "," + longitude +
                        "&title=" + title + "&content=" + content + "&output=html&src=" + appName
            )
            val loction = Intent(Intent.ACTION_VIEW, mapUri)
            activity.startActivity(loction)
        } else {
            TbWebViewActivity.startActivity(
                activity,
                title,
                "http://api.map.baidu.com/marker?location=" + latitude + "," + longitude +
                        "&title=" + title + "&content=" + content + "&output=html&src=" + appName
            )
        }
    }
    /**
     * 打开网页版 导航
     *
     * @param activity
     * @param region    当给定region时，认为起点和终点都在同一城市，除非单独给定起点或终点的城市。
     * @param appName
     * @param useOutWeb 是否调用外部的浏览器 默认不调用
     */
    /**
     * 打开网页版 导航
     *
     * @param activity
     * @param region   当给定region时，认为起点和终点都在同一城市，除非单独给定起点或终点的城市。
     * @param appName
     */
    @JvmOverloads
    fun openBrowserNaviMap(
        activity: Context,
        sLongitude: String,
        sLatitude: String,
        sName: String,
        dLongitude: String,
        dLatitude: String,
        dName: String,
        region: String,
        appName: String,
        useOutWeb: Boolean = false
    ) {
        val uriString = "http://api.map.baidu.com/direction?origin=latlng:" +
                sLatitude + "," + sLongitude + "|name:" + sName + "&destination=latlng:" +
                dLatitude + "," + dLongitude + "|name:" + dName + "&mode=driving&region=" + region +
                "&output=html&src=" + appName
        if (useOutWeb) {
            val mapUri = Uri.parse(uriString)
            val location = Intent(Intent.ACTION_VIEW, mapUri)
            activity.startActivity(location)
        } else {
            TbWebViewActivity.startActivity(activity, "导航", uriString)
        }
    }

    /**
     * 显示对话框
     */
    fun showAlertDialog(
        activity: Activity, type: Int, showGaode: Boolean,
        showBaidu: Boolean, longitude: String,
        latitude: String, title: String,
        dLongitude: String,
        dLatitude: String, dName: String,
        content: String, appName: String
    ) {
        menuWindow = SelectPopupWindow(activity, object : OnPopWindowClickListener {
            override fun onPopWindowClickListener(view: View) {
                if (view.id == R.id.btn_select_baidu) {
                    //百度
                    when (type) {
                        TYPE_MAPVIEW_WITH_TIPS -> {
                            openBaiduMarkerMap(activity, longitude, latitude, title, content)
                        }
                        TYPE_MAPVIEW_DIRECTION -> {
                            openBaiduDirectionMap(
                                activity,
                                longitude,
                                latitude,
                                title,
                                dLongitude,
                                dLatitude,
                                dName
                            )
                        }
                        TYPE_MAPVIEW_NAVI -> {
                            openBaiduNaviMap(activity, longitude, latitude)
                        }
                    }
                } else if (view.id == R.id.btn_select_gaode) {
                    //高德
                    when (type) {
                        TYPE_MAPVIEW_WITH_TIPS -> {
                            openGaodeMarkerMap(activity, longitude, latitude, appName, title)
                        }
                        TYPE_MAPVIEW_DIRECTION -> {
                            openGaodeRouteMap(
                                activity,
                                longitude,
                                latitude,
                                title,
                                dLongitude,
                                dLatitude,
                                dName,
                                appName
                            )
                        }
                        TYPE_MAPVIEW_NAVI -> {
                            openGaodeNaviMap(activity, appName, title, latitude, longitude)
                        }
                    }
                }
            }
        }, showGaode, showBaidu)
        val rect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        val winHeight = activity.window.decorView.height
        menuWindow!!.showAtLocation(
            activity.window.decorView,
            Gravity.BOTTOM,
            0,
            winHeight - rect.bottom
        )
    }

    /**
     * 通过包名获取应用信息
     *
     * @param context
     * @param packageName
     * @return
     */
    private fun getAppInfoByPak(context: Context, packageName: String): String? {
        val packageManager = context.packageManager
        val packageList = packageManager.getInstalledPackages(0)
        for (packageInfo in packageList) {
            if (packageName == packageInfo.packageName) {
                return packageName
            }
        }
        return null
    }

    /**
     * 返回当前设备上的地图应用集合
     *
     * @param context
     * @return
     */
    private fun getMapApps(context: Context): List<String> {
        val apps = LinkedList<String>()
        for (pak in SUPPORT_MAP_APP) {
            val appinfo = getAppInfoByPak(context, pak)
            if (appinfo != null) {
                apps.add(appinfo)
            }
        }
        return apps
    }
}