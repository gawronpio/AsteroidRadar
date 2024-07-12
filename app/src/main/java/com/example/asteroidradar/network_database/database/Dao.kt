package com.example.asteroidradar.network_database.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDatabaseDao {
    @Insert
    suspend fun insert(asteroid: AsteroidData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: AsteroidData)

    @Query("SELECT * from asteroid_table WHERE id = :key")
    suspend fun get(key: Long): AsteroidData?

    @Query("SELECT * FROM asteroid_table WHERE close_approach_date >= :startDate ORDER BY close_approach_date")
    suspend fun getAll(startDate: Long = System.currentTimeMillis()): List<AsteroidData>

    @Query("DELETE FROM asteroid_table WHERE close_approach_date < :date")
    suspend fun deleteOld(date: Long = System.currentTimeMillis())
}