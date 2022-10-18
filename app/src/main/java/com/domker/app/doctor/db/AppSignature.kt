package com.domker.app.doctor.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.util.*

/**
 * 存储App签名
 */
@Entity(tableName = "app_signature")
class AppSignature {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "package_name")
    @NotNull
    var packageName: String = ""

    @ColumnInfo(name = "origin_signature")
    var originSignature: ByteArray? = null

    @ColumnInfo(name = "md5_signature")
    var md5Signature: String? = null

    @ColumnInfo(name = "sha1_signature")
    var sha1Signature: String? = null

    @ColumnInfo(name = "sha256_signature")
    var sha256Signature: String? = null

    @ColumnInfo(name = "create_time")
    var createTime: Long = Date().time
}
