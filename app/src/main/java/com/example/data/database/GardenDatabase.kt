package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GardenPlant::class], version = 1, exportSchema = false)
abstract class GardenDatabase : RoomDatabase() {
    abstract fun gardenDao(): GardenDao

    companion object {
        @Volatile
        private var INSTANCE: GardenDatabase? = null

        fun getDatabase(context: Context): GardenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GardenDatabase::class.java,
                    "garden_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
