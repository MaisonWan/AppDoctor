package com.domker.base.device

import android.Manifest.permission.READ_PHONE_STATE
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.format.Formatter
import android.util.DisplayMetrics
import android.view.WindowManager
import com.domker.base.SystemVersion
import com.domker.base.addPair
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*
import java.util.regex.Pattern


private const val NOT_AVAILABLE = "not available"

/**
 * 手机信息
 *
 * @author maisonwan
 * @date 2013-12-16 上午11:34:12
 */
class DeviceManager(private val context: Context) {
    private val mTelephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    /**
     * 获取关于本机的信息
     */
    fun getDeviceAbout(): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        list.addPair("系统版本", systemVersion)
        list.addPair("Android ID", androidID)
        list.addPair("WLAN Mac地址", macAddress)

        getScreenSize().apply {
            list.addPair("屏幕尺寸", "${this.first} * ${this.second}")
        }

        list.addPair("Scaled Density", dpiDensity)
        list.addPair("像素密度DPI", densityDpi.toString())
        list.addPair("Density", density.toString())

        list.addAll(osBuildInfo)
        return list
    }

    /**
     * 检测是否有相关权限
     */
    fun checkPermission(): Boolean {
        getPermissions().forEach {
            return context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
        return true
    }

    /**
     * 获取当前所需的全部的权限列表
     */
    fun getPermissions(): Array<String> {
        return arrayOf(READ_PHONE_STATE)
    }

    /**
     * 得到应用的堆栈信息，返回单位是kb
     *
     * @return int
     */
    val appMaxHeapSize: Long
        get() = Runtime.getRuntime().maxMemory() / 1024

    val appTotalHeapSize: Long
        get() = Runtime.getRuntime().totalMemory() / 1024

    val appFreeHeapSize: Long
        get() = Runtime.getRuntime().freeMemory() / 1024

    val imei: String
        @SuppressLint("HardwareIds")
        get() = mTelephonyManager.deviceId ?: NOT_AVAILABLE

    @SuppressLint("HardwareIds")
    fun getIMEI(index: Int): String {
        return mTelephonyManager.getDeviceId(index) ?: NOT_AVAILABLE
    }

    val macAddress: String
        @SuppressLint("MissingPermission")
        get() {
            val wifi =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val info = wifi.connectionInfo
            return if (info != null && info.macAddress != null) {
                info.macAddress
            } else NOT_AVAILABLE
        }

    val androidID: String
        @SuppressLint("HardwareIds")
        get() = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    val imsi: String
        @SuppressLint("HardwareIds")
        get() {
            return mTelephonyManager.subscriberId ?: NOT_AVAILABLE
        }

    /**
     * 系统sdk版本号
     *
     * @return String
     */
    private val systemVersion: String
        get() = SystemVersion.getShowLabel(SystemVersion.getVersion(Build.VERSION.SDK_INT))

    private val osBuildInfo: List<Pair<String, String>>
        get() {
            val data: MutableList<Pair<String, String>> = ArrayList()
            data.addPair("设备型号", Build.PRODUCT)
            data.addPair("CPU架构", cpuABI)
            data.addPair("处理器", Build.HARDWARE)
            data.addPair("BOOTLOADER", Build.BOOTLOADER)
            data.addPair("TAGS", Build.TAGS)
            data.addPair("VERSION_CODES.BASE", Build.VERSION_CODES.BASE.toString())
            data.addPair("MODEL", Build.MODEL)
            data.addPair("VERSION.RELEASE", Build.VERSION.RELEASE)
            data.addPair("DEVICE", Build.DEVICE)
            data.addPair("DISPLAY", Build.DISPLAY)
            data.addPair("BRAND", Build.BRAND)
            data.addPair("BOARD", Build.BOARD)
            data.addPair("FINGERPRINT", Build.FINGERPRINT)
            data.addPair("ID", Build.ID)
            data.addPair("MANUFACTURER", Build.MANUFACTURER)
            data.addPair("USER", Build.USER)
            return data
        }

    private val cpuABI: String
        get() = Arrays.toString(Build.SUPPORTED_ABIS)

    /**
     * 得到屏幕分辨率
     *
     * @return Pair<Int, Int>
     */
    fun getScreenSize(): Pair<Int, Int> {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val dm = wm.currentWindowMetrics.bounds
            Pair(dm.width(), dm.height())
        } else {
            val dm = DisplayMetrics()
            wm.defaultDisplay.getRealMetrics(dm)
            Pair(dm.widthPixels, dm.heightPixels)
        }
    }

    val densityDpi: Int
        get() {
            val dm = context.resources.displayMetrics
            return dm.densityDpi
        }

    val density: Float
        get() {
            val dm = context.resources.displayMetrics
            return dm.density
        }

    val dpiDensity: String
        get() {
            val dm = context.resources.displayMetrics
            return String.format("%f * %f", dm.xdpi, dm.ydpi)
        }


    /**
     * 获取当年设备的状态信息汇总
     */
    fun getDeviceState(): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()

        list.addPair("Total Memory", totalMemory)
        list.addPair("Avail Memory", availMemory)
        appMaxHeapSize.apply {
            list.addPair(
                "Max Heap Size",
                "${this}KB(${Formatter.formatFileSize(context, this * 1024)})"
            )
        }
        appFreeHeapSize.apply {
            list.addPair(
                "Free Heap Size",
                "${this}KB(${Formatter.formatFileSize(context, this * 1024)})"
            )
        }
        appTotalHeapSize.apply {
            list.addPair(
                "Total Heap Size",
                "${this}KB(${Formatter.formatFileSize(context, this * 1024)})"
            )
        }
        return list
    }

    val sDCardPath: String?
        get() = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            Environment.getExternalStorageDirectory().toString()
        } else null

    /**
     * 获取android当前可用内存大小
     * mi.availMem; 当前系统的可用内存
     * 将获取的内存大小规格化
     */
    val availMemory: String
        get() { // 获取android当前可用内存大小
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val mi = ActivityManager.MemoryInfo()
            am.getMemoryInfo(mi)
            //mi.availMem; 当前系统的可用内存  
            return Formatter.formatFileSize(context, mi.availMem) // 将获取的内存大小规格化  
        }// 读取meminfo第一行，系统总内存大小

    /**
     * 获得系统总内存，单位是KB，乘以1024转换为Byte
     * Byte转换为KB或者MB，内存大小规格化
     * 系统内存信息文件
     */
    val totalMemory: String
        get() {
            val str1 = "/proc/meminfo" // 系统内存信息文件
            val str2: String
            var arrayOfString = ""
            var initialMemory: Long = 0
            try {
                val localFileReader = FileReader(str1)
                val localBufferedReader = BufferedReader(localFileReader, 8192)
                localBufferedReader.readLines().forEach {
                    println(it)
                }
                str2 = localBufferedReader.readLine() // 读取meminfo第一行，系统总内存大小
                val matcher = Pattern.compile("\\d+").matcher(str2)
                if (matcher.find()) {
                    arrayOfString = matcher.group()
                }

                initialMemory = arrayOfString.toLong() * 1024 // 获得系统总内存，单位是KB，乘以1024转换为Byte
                localBufferedReader.close()
            } catch (_: IOException) {
            }
            return Formatter.formatFileSize(context, initialMemory) // Byte转换为KB或者MB，内存大小规格化
        }

}