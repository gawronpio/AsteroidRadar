package com.example.asteroidradar.network_database.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asteroid_table")
data class AsteroidData(
    @PrimaryKey(autoGenerate = false)
    var id: Long = 0L,

    @ColumnInfo(name = "close_approach_date")
    val date: Long = 0L,

    @ColumnInfo(name = "absolute_magnitude")
    var maxMagnitude: Long = 0L,

    @ColumnInfo(name = "estimated_diameter")
    var diameter: Long = 0L,

    @ColumnInfo(name = "relative_velocity")
    var velocity: Long = 0L,

    @ColumnInfo(name = "distance_from_earth")
    var distance: Long = 0L
)

data class Asteroid(val id: Long,
                    val date: Long,
                    val maxMagnitude: Long,
                    val diameter: Long,
                    val velocity: Long,
                    val distance: Long)

fun List<AsteroidData>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            date = it.date,
            maxMagnitude = it.maxMagnitude,
            diameter = it.diameter,
            velocity = it.velocity,
            distance = it.distance)
    }
}