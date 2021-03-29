package com.ahmetgezici.populartvshow.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmetgezici.populartvshow.model.database.FavoriteTv
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [FavoriteTv::class], version = 1)
abstract class FavoriteTvDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoriteTvDao?

    companion object {

        var instance: FavoriteTvDatabase? = null

        private const val NUMBER_OF_THREADS = 4
        val databaseExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        @Synchronized
        fun getDatabase(context: Context): FavoriteTvDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteTvDatabase::class.java,
                    "favorites_database"
                ).fallbackToDestructiveMigration()
                    .build()
            }

            return instance as FavoriteTvDatabase
        }

    }


}