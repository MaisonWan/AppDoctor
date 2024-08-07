package com.domker.doctor.main.photo

/**
 * 相片实体类
 * Created by wanlipeng on 2021/5/12 6:46 下午
 */
class PhotoExif {
    // Exif版本
    var exifVersion = ""

    //　　TAG_IMAGE_WIDTH：图片宽度。
    var width: Int = 0

    //　　TAG_IMAGE_LENGTH：图片高度。
    var height: Int = 0

    // 分辨率单元，以及X和Y方向的单元像素数
    var resolutionUnit = 0
    var xResolution = 0.0
    var yResolution = 0.0

    // 色彩空间
    var colorSpace: Int = 0

    // 处理或者生成的软件
    var software: String = ""

    val camera = Camera()

    var gps = GPS()

    class Camera {
        // TAG_APERTURE：光圈值。
        var aperture: Double = 0.0

        //　　TAG_DATETIME：拍摄时间，取决于设备设置的时间。
        var dateTime: Long = 0
        var dateTimeDigitized: Long = 0

        // TAG_METERING_MODE
        var meteringMode: Short = 0

        //　　TAG_EXPOSURE_TIME：曝光时间。
        var exposureTime: Double = 0.0

        // 曝光补偿
        var exposureBiasValue = 0.0

        // 曝光模式
        var exposureProgram: Short = 0

        //　　TAG_FLASH：闪光灯。
        var flash: Int = 0

        //　　TAG_FOCAL_LENGTH：焦距。
        var focalLength: String = ""

        //　　TAG_ISO：ISO。
        var ios: Int = 0

        // TAG_WHITE_BALANCE
        var whiteBalance = ""

        //　　TAG_MAKE：设备品牌。
        var make: String = ""

        //　　TAG_MODEL：设备型号，整形表示，在ExifInterface中有常量对应表示。
        var model: String = ""

        //　　TAG_ORIENTATION：旋转角度，整形表示，在ExifInterface中有常量对应表示。
        var orientation: Int = 0

        // TAG_ARTIST
        var artists: String = ""
    }

    /**
     * GPS定位相关信息
     */
    class GPS {
        // 具体的位置标示
        val location = GPSLocation()

        // TAG_GPS_LATITUDE 纬度
//        var latitude = ""

        // TAG_GPS_LATITUDE_REF 纬度参考
        var latitudeRef = ""

        // TAG_GPS_LONGITUDE 经度
//        var longitude = ""

        // TAG_GPS_LONGITUDE_REF 经度参考
        var longitudeRef = ""

        //ExifInterface.TAG_GPS_ALTITUDE //海拔高度
        var altitude = 0.0

        //ExifInterface.TAG_GPS_ALTITUDE_REF //海拔高度
        var altitudeRef = ""

        var gpsDateTime: Long = 0
    }

    /**
     * GPS的经纬度信息
     */
    class GPSLocation {
        private var latitude = DoubleArray(3) { 0.0 }

        private var longitude = DoubleArray(3) { 0.0 }

        /**
         * 解析纬度
         */
        fun parserLatitude(la: String) {
            la.split(",").forEachIndexed { index, s ->
                val num = s.split("/")
                try {
                    latitude[index] = num[0].toDouble() / num[1].toDouble()
                } catch (e: Exception) {
                    latitude[index] = 0.0
                }
            }
        }

        /**
         * 解析经度
         */
        fun parserLongitude(lo: String) {
            lo.split(",").forEachIndexed { index, s ->
                val num = s.split("/")
                try {
                    longitude[index] = num[0].toDouble() / num[1].toDouble()
                } catch (e: Exception) {
                    longitude[index] = 0.0
                }
            }
        }

        fun getLatitude(): Double {
            return latitude[0] + (latitude[1] + latitude[2] / 60.0) / 60.0
        }

        fun getLongitude(): Double {
            return longitude[0] + (longitude[1] + longitude[2] / 60.0) / 60.0
        }
    }
}
