package com.ahmetgezici.populartvshow.model.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tv")
data class FavoriteTv(

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "uid")
    var uid: Int = 0

)