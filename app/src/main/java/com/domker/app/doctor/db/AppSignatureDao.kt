package com.domker.app.doctor.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.jetbrains.annotations.NotNull

/**
 * 签名相关的存储和查询
 */
@Dao
interface AppSignatureDao {

    @Query("select * from app_signature")
    fun allAppSignature(): List<AppSignature>

    @Query("select * from app_signature where package_name = :packageName")
    fun appSignatures(packageName: String): LiveData<AppSignature>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAppSignature(@NotNull appSignature: AppSignature)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAppSignatures(@NotNull appSignatures: Array<AppSignature>)
}