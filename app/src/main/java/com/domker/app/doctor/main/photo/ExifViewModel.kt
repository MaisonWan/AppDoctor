package com.domker.app.doctor.main.photo

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.domker.app.doctor.util.DataFormat
import com.domker.base.thread.AppExecutors

/**
 *
 * Created by wanlipeng on 2021/5/12 8:59 下午
 */
class ExifViewModel : ViewModel() {

    private val exif = MutableLiveData<List<ExifItem>>()

    fun getExif(): LiveData<List<ExifItem>> = exif

    /**
     * 异步读取指定位置的图片，并且分析EXIF信息
     */
    fun parserExif(activity: Activity, uri: Uri) {
        AppExecutors.executor.execute {
            val stream = activity.contentResolver.openInputStream(uri)
            val parser = PhotoExifParser(stream!!)
            val photoExif = parser.loadExif()
            exif.postValue(convert(photoExif))
        }
    }

    /**
     * 将Exif信息的实体，转化为KV的值
     */
    private fun convert(exif: PhotoExif): List<ExifItem> {
        val data = mutableListOf<ExifItem>()
        data.add(ExifItem("EXIF版本", exif.exifVersion))
        data.add(ExifItem("分辨率", "${exif.width} x ${exif.height}px"))
        data.add(ExifItem("分辨率单元", exif.resolutionUnit.toString()))
        data.add(ExifItem("X像素密度", exif.xResolution.toString()))
        data.add(ExifItem("Y像素密度", exif.yResolution.toString()))

        data.add(ExifItem("软件名称", exif.software))
        data.add(ExifItem("拍摄时间", DataFormat.getFormatFullDate(exif.camera.dateTime)))
        data.add(ExifItem("数字化时间", DataFormat.getFormatFullDate(exif.camera.dateTimeDigitized)))
        data.add(ExifItem("设备型号", exif.camera.model))
        data.add(ExifItem("设备品牌", exif.camera.make))
        data.add(ExifItem("焦距", "${exif.camera.focalLength} mm"))
        data.add(ExifItem("测光模式", DataFormat.meteringMode(exif.camera.meteringMode)))
        data.add(ExifItem("光圈", "f/${exif.camera.aperture}"))
        data.add(ExifItem("曝光时长", DataFormat.getDoubleShort(exif.camera.exposureTime) + "s"))
        data.add(ExifItem("曝光补偿", exif.camera.exposureBiasValue.toString()))
        data.add(ExifItem("曝光模式", DataFormat.exposureMode(exif.camera.exposureProgram)))
        data.add(ExifItem("ISO感光度", exif.camera.ios.toString()))
        data.add(ExifItem("色彩空间", DataFormat.colorSpace(exif.colorSpace)))

        data.add(ExifItem("白平衡", exif.camera.whiteBalance))
        data.add(ExifItem("闪光灯", exif.camera.flash.toString()))
        data.add(ExifItem("旋转角度", "${exif.camera.orientation}°"))
        data.add(ExifItem("GPS定位", "", TYPE_LOCATION, exif.gps))

        data.add(ExifItem("GPS时间", DataFormat.getFormatFullDate(exif.gps.gpsDateTime)))
        data.add(ExifItem("海拔高度", "${exif.gps.altitudeRef}${exif.gps.altitude} M"))

        return data
    }
}