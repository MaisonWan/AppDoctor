package com.domker.app.doctor.db

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * App数据的操作类
 * Created by wanlipeng on 2020/6/12 10:09 AM
 */
@Dao
interface AppDataDao {

    @Query("select * from app_data")
    fun allAppData(): List<AppEntity>

    @Query("select * from app_data")
    fun appDataWithUpdateTime(): LiveData<List<AppEntity>>

    @Query("select * from app_data where package_name = :packageName")
    fun appDataByPackageName(packageName: String): LiveData<AppEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAppData(appEntity: AppEntity): Int

    @Update
    fun updateAppData(appEntityList: List<AppEntity>): Int

    @Delete
    fun deleteAppData(appEntity: AppEntity): Int

    @Delete
    fun deleteAppData(appEntity: List<AppEntity>): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAppData(appEntity: AppEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAppData(appEntityList: List<AppEntity>)
}