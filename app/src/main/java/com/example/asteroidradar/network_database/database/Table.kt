package com.example.asteroidradar.network_database.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asteroid_table")
data class AsteroidData(
    @PrimaryKey(autoGenerate = false)
    var id: Long = 0L,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "close_approach_date")
    val date: Long = 0L,

    @ColumnInfo(name = "absolute_magnitude")
    var maxMagnitude: Double = 0.0,

    @ColumnInfo(name = "estimated_diameter")
    var diameter: Double = 0.0,

    @ColumnInfo(name = "relative_velocity")
    var velocity: Double = 0.0,

    @ColumnInfo(name = "distance_from_earth")
    var distance: Double = 0.0,

    @ColumnInfo(name = "is_potentially_hazardous")
    var hazardous: Boolean = false
)

data class Asteroid(val id: Long,
                    val name: String,
                    val date: Long,
                    val maxMagnitude: Double,
                    val diameter: Double,
                    val velocity: Double,
                    val distance: Double,
                    val hazardous: Boolean)

fun List<AsteroidData>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            name = it.name,
            date = it.date,
            maxMagnitude = it.maxMagnitude,
            diameter = it.diameter,
            velocity = it.velocity,
            distance = it.distance,
            hazardous = it.hazardous)
    }
}