package com.domker.doctor.main.photo

import androidx.exifinterface.media.ExifInterface
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale


/**
 * Exif信息解析器
 * Created by wanlipeng on 2021/5/12 6:40 下午
 */
class PhotoExifParser(stream: InputStream) {
    private var exifInterface: ExifInterface = ExifInterface(stream)
    val sFormatter = SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault())

    /**
     * 加载图片Exif信息
     */
    fun loadExif(): PhotoExif {
        /**
         * 文件名: IMG_3511.JPG
        位置: /Users/wanlipeng/Pictures/9周年
        尺寸: 5472 x 3648
        文件大小: 4.4MB (4,405,976)
        创建日期: 2021/3/30 下午4:21:38
        修改日期: 2021/3/30 下午5:11:20
        相机制造商: Canon
        相机型号: Canon EOS 6D
        镜头型号: EF24-105mm f/4L IS USM
        镜头规格: 24-105mm F/0
        光圈大小: F4
        光圈: F4
        焦距: 28 mm
        快门速度: 1/166 s
        曝光时间: 1/160 s
        曝光补偿: 0 EV
        ISO 感光度: 800
        软件: PhotoScape
        原始日期时间: 2021/3/30 下午4:21:36
        数字化日期时间: 2021/3/30 下午4:21:36
        白平衡: Auto
        测光模式: Multi-segment
        闪光灯: No, compulsory
        曝光程序: Aperture priority
        曝光模式: Auto
        色彩空间: sRGB
        X 维度像素: 5472
        Y 维度像素: 3648
        X 分辨率: 72
        Y 分辨率: 72
        分辨率单位: inch
         */
        val exif = PhotoExif()
        //　　TAG_IMAGE_WIDTH：图片宽度。
        exif.width = getInt(ExifInterface.TAG_IMAGE_WIDTH)

        //　　TAG_IMAGE_LENGTH：图片高度。
        exif.height = getInt(ExifInterface.TAG_IMAGE_LENGTH)

        // 分辨率
        exif.resolutionUnit = getInt(ExifInterface.TAG_RESOLUTION_UNIT)
        exif.xResolution = getDouble(ExifInterface.TAG_X_RESOLUTION)
        exif.yResolution = getDouble(ExifInterface.TAG_Y_RESOLUTION)

        exif.colorSpace = getInt(ExifInterface.TAG_COLOR_SPACE)

        exif.software = getString(ExifInterface.TAG_SOFTWARE)

        exif.exifVersion = getString(ExifInterface.TAG_EXIF_VERSION)

        loadCamera(exif)
        loadGps(exif)

        return exif
    }

    private fun loadCamera(exif: PhotoExif) {
        //　　TAG_MODEL：设备型号，整形表示，在ExifInterface中有常量对应表示。
        exif.camera.model = getString(ExifInterface.TAG_MODEL)

        // TAG_APERTURE：光圈值。
        exif.camera.aperture = getDouble(ExifInterface.TAG_F_NUMBER)

        //　　TAG_DATETIME：拍摄时间，取决于设备设置的时间。

        // The exif field is in local time. Parsing it as if it is UTC will yield time
        // since 1/1/1970 local time
        exif.camera.dateTime = getTimeStamp(ExifInterface.TAG_DATETIME)

        exif.camera.dateTimeDigitized = getTimeStamp(ExifInterface.TAG_DATETIME)

        // 测光模式
        exif.camera.meteringMode = getShort(ExifInterface.TAG_METERING_MODE)

        //　　TAG_EXPOSURE_TIME：曝光时间。
        exif.camera.exposureTime = getDouble(ExifInterface.TAG_EXPOSURE_TIME)

        // 曝光补偿
        exif.camera.exposureBiasValue = getDouble(ExifInterface.TAG_EXPOSURE_BIAS_VALUE)

        // TAG_EXPOSURE_PROGRAM
        exif.camera.exposureProgram = getShort(ExifInterface.TAG_EXPOSURE_PROGRAM)

        //　　TAG_FLASH：闪光灯。
        exif.camera.flash = getInt(ExifInterface.TAG_FLASH)

        //　　TAG_FOCAL_LENGTH：焦距。
        exif.camera.focalLength = getString(ExifInterface.TAG_FOCAL_LENGTH)

        //　　TAG_ISO：ISO。
        exif.camera.ios = getInt(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY)

        // TAG_WHITE_BALANCE
        if (getShort(ExifInterface.TAG_WHITE_BALANCE) == ExifInterface.WHITE_BALANCE_AUTO) {
            exif.camera.whiteBalance = "Auto"
        } else {
            exif.camera.whiteBalance = "手动"
        }

        //　　TAG_MAKE：设备品牌。
        exif.camera.make = getString(ExifInterface.TAG_MAKE)

        //　　TAG_ORIENTATION：旋转角度，整形表示，在ExifInterface中有常量对应表示。
        exif.camera.orientation = parserOrientation(getInt(ExifInterface.TAG_ORIENTATION))

        // TAG_ARTIST
        exif.camera.artists = getString(ExifInterface.TAG_SOFTWARE)
    }

    private fun loadGps(exif: PhotoExif) {
        exif.gps.location.parserLatitude(getString(ExifInterface.TAG_GPS_LATITUDE))
        exif.gps.latitudeRef = getString(ExifInterface.TAG_GPS_LATITUDE_REF)

        exif.gps.location.parserLongitude(getString(ExifInterface.TAG_GPS_LONGITUDE))
        exif.gps.longitudeRef = getString(ExifInterface.TAG_GPS_LONGITUDE_REF)

        // 解析GPS的时间
        exif.gps.gpsDateTime = parserExifDatetime(
            "${getString(ExifInterface.TAG_GPS_DATESTAMP)} ${
                getString(ExifInterface.TAG_GPS_TIMESTAMP)
            }"
        )

        //海拔高度
        exif.gps.altitude = getDouble(ExifInterface.TAG_GPS_ALTITUDE)

        //海拔高度
        exif.gps.altitudeRef =
            if (getShort(ExifInterface.TAG_GPS_ALTITUDE_REF) == ExifInterface.ALTITUDE_ABOVE_SEA_LEVEL) {
                "+"
            } else {
                "-"
            }

    }

    /**
     * 解析旋转角度
     */
    private fun parserOrientation(rotate: Int): Int {
        return when (rotate) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    private fun getLong(tag: String): Long {
        return exifInterface.getAttribute(tag)?.toLong() ?: 0
    }

    private fun getInt(tag: String): Int {
        return exifInterface.getAttributeInt(tag, 0)
    }

    private fun getShort(tag: String): Short {
        return exifInterface.getAttribute(tag)?.toShort() ?: 0
    }

    private fun getInt(tag: String, defaultValue: Int): Int {
        return exifInterface.getAttributeInt(tag, defaultValue)
    }

    private fun getDouble(tag: String): Double {
        return exifInterface.getAttributeDouble(tag, 0.0)
    }

    private fun getString(tag: String): String {
        return exifInterface.getAttribute(tag) ?: ""
    }

    private fun getTimeStamp(tag: String): Long {
        return parserExifDatetime(getString(tag))
    }

    private fun parserExifDatetime(time: String): Long {
        return try {
            val datetime = sFormatter.parse(time)
            datetime?.time ?: 0
        } catch (e: Exception) {
            0
        }
    }
}