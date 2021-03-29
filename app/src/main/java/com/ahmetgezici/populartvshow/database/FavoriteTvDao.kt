package com.ahmetgezici.populartvshow.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmetgezici.populartvshow.model.database.FavoriteTv

@Dao
interface FavoriteTvDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavoriteTv(favoriteTv: FavoriteTv)

    @Query("SELECT * FROM favorite_tv")
    fun getFavorites(): LiveData<List<FavoriteTv>>

    @Query("DELETE FROM favorite_tv")
    fun deleteAllFavorites()

    @Query("DELETE FROM favorite_tv WHERE id=:id")
    fun deleteFavoriteTv(id: Int)

}