package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "garden_plants")
data class GardenPlant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val scientificName: String,
    val variety: String,
    val addedDate: Long = System.currentTimeMillis(),
    val lastWateredDate: Long = System.currentTimeMillis(),
    val wateringIntervalDays: Int = 3,
    val notes: String = "",
    val status: String = "Healthy",
    val imageUrl: String? = null
)
