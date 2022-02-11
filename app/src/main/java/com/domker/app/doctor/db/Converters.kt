package com.domker.app.doctor.db

import androidx.room.TypeConverter
import java.util.*


/**
 * 数据库存储类型转化
 * Created by wanlipeng on 2020/6/12 10:17 AM
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

//    @TypeConverter
//    fun drawableToByte(drawable: Drawable?): ByteArray? {
//        drawable?.apply {
//            val os = ByteArrayOutputStream()
//            (drawable as AdaptiveIconDrawable).bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
//            return os.toByteArray()
//        }
//        return null
//    }
//
//    @TypeConverter
//    fun byteToDrawable(byteArray: ByteArray?): Drawable? {
//        byteArray?.apply {
//            return BitmapDrawable(BitmapFactory.decodeByteArray(this, 0, this.size))
//        }
//        return null
//    }
}