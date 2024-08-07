package com.domker.doctor.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * 数据库存储等操作
 * Created by wanlipeng on 2020/6/12 10:09 AM
 */
@Database(entities = [AppEntity::class, AppSignature::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * app db操作
     */
    abstract fun appDataDao(): AppDataDao

    /**
     * 签名相关的数据库操作
     */
    abstract fun appSignatureDao(): AppSignatureDao
}