package com.example.asteroidradar.network_database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asteroidradar.network_database.database.Asteroid
import com.example.asteroidradar.network_database.database.AsteroidDatabase
import com.example.asteroidradar.network_database.database.asDomainModel
import com.example.asteroidradar.network_database.network.NetworkApi
import com.example.asteroidradar.dateMillis2String
import com.example.asteroidradar.network_database.network.asteroidsDataList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class AsteroidRepository(private val database: AsteroidDatabase) {
    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>> = _asteroids

    init {
        CoroutineScope(Dispatchers.IO).launch {
            _asteroids.postValue(database.asteroidDatabaseDao.getAll().asDomainModel())
        }
    }

    private suspend fun updateAsteroids() {
        _asteroids.postValue(database.asteroidDatabaseDao.getAll().asDomainModel())
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroidsData = NetworkApi.retrofitService.getFeed(
                    dateMillis2String(System.currentTimeMillis()),
                    dateMillis2String(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000), // Add 7 days
                )
                database.asteroidDatabaseDao.insertAll(*asteroidsData.asteroidsDataList())
            } catch(e: Exception) {
                Timber.e(e.message)
                // TODO: Dodać akcje w przypadku nieudanego pobrania danych?
            }
        }
    }

    suspend fun deleteOldAsteroids() {
        withContext(Dispatchers.IO) {
            database.asteroidDatabaseDao.deleteOld()
        }
    }
}