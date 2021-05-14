package com.domker.app.doctor.main.photo

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

    val camera = Camera()

    var gps = GPS()

    class Camera {
        // TAG_APERTURE：光圈值。
        var aperture: Double = 0.0

        //　　TAG_DATETIME：拍摄时间，取决于设备设置的时间。
        var dateTime: Long = 0
        var dateTimeDigitized: Long = 0

        //　　TAG_EXPOSURE_TIME：曝光时间。
        var exposureTime: Double = 0.0

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
    }

    /**
     * GPS定位相关信息
     */
    class GPS {
        // TAG_GPS_LATITUDE 纬度
        var latitude = ""

        // TAG_GPS_LATITUDE_REF 纬度参考
        var latitudeRef = ""

        // TAG_GPS_LONGITUDE 经度
        var longitude = ""

        // TAG_GPS_LONGITUDE_REF 经度参考
        var longitudeRef = ""

        //ExifInterface.TAG_GPS_ALTITUDE //海拔高度
        var altitude = 0.0
        //ExifInterface.TAG_GPS_ALTITUDE_REF //海拔高度
        var altitudeRef = ""

        var gpsDateTime: Long = 0
    }
}
