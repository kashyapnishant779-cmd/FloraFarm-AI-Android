package com.example.data.database

import kotlinx.coroutines.flow.Flow

class GardenRepository(private val gardenDao: GardenDao) {
    val allPlants: Flow<List<GardenPlant>> = gardenDao.getAllPlants()

    suspend fun getPlantById(id: Int): GardenPlant? {
        return gardenDao.getPlantById(id)
    }

    suspend fun insertPlant(plant: GardenPlant) {
        gardenDao.insertPlant(plant)
    }

    suspend fun updatePlant(plant: GardenPlant) {
        gardenDao.updatePlant(plant)
    }

    suspend fun deletePlant(plant: GardenPlant) {
        gardenDao.deletePlant(plant)
    }
}
