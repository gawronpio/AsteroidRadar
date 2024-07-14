package com.example.asteroidradar.network_database

import com.example.asteroidradar.network_database.database.AsteroidDatabase
import com.example.asteroidradar.network_database.network.NetworkApi
import com.example.asteroidradar.dateMillis2String
import com.example.asteroidradar.network_database.network.asteroidsDataList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class AsteroidRepository(private val database: AsteroidDatabase) {
    suspend fun refreshAsteroids(): Boolean {
        withContext(Dispatchers.IO) {
            try {
                val asteroidsData = NetworkApi.retrofitService.getFeed(
                    dateMillis2String(System.currentTimeMillis()),
                    dateMillis2String(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000), // Add 7 days
                )
                database.asteroidDatabaseDao.insertAll(*asteroidsData.asteroidsDataList())
            } catch(e: Exception) {
                Timber.e("Network problem: ${e.message}")
                return@withContext false
            }
        }
        return true
    }

    suspend fun deleteOldAsteroids() {
        withContext(Dispatchers.IO) {
            database.asteroidDatabaseDao.deleteOld()
        }
    }
}