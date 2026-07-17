package com.example.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GardenDao {
    @Query("SELECT * FROM garden_plants ORDER BY addedDate DESC")
    fun getAllPlants(): Flow<List<GardenPlant>>

    @Query("SELECT * FROM garden_plants WHERE id = :id")
    suspend fun getPlantById(id: Int): GardenPlant?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: GardenPlant)

    @Update
    suspend fun updatePlant(plant: GardenPlant)

    @Delete
    suspend fun deletePlant(plant: GardenPlant)
}
