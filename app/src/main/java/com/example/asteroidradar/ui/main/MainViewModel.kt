package com.example.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.asteroidradar.network_database.AsteroidRepository
import com.example.asteroidradar.network_database.database.AsteroidData
import com.example.asteroidradar.network_database.database.AsteroidDatabase.Companion.getDatabase
import com.example.asteroidradar.network_database.database.AsteroidDatabaseDao
import com.example.asteroidradar.network_database.network.NetworkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainViewModel(databaseDao: AsteroidDatabaseDao, application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    private var _apodUrl = MutableLiveData<String>()
    val apodUrl: LiveData<String>
        get() = _apodUrl
    private var _apodTitle = MutableLiveData<String>()
    val apodTitle: LiveData<String>
        get() = _apodTitle
    private var _missingApod = MutableLiveData(false)
    val missingApod: LiveData<Boolean>
        get() = _missingApod
    private var _navigateToAsteroidDetail = MutableLiveData<Long?>()
    val navigateToAsteroidDetail: LiveData<Long?>
        get() = _navigateToAsteroidDetail

    val asteroids = asteroidRepository.asteroids
    private var _asteroidsData = MutableLiveData<List<AsteroidData>>()
    val asteroidsData: LiveData<List<AsteroidData>>
        get() = _asteroidsData

    init {
        viewModelScope.launch {
            getNewPicOfTheDayUrl()
            asteroidRepository.refreshAsteroids()
            _asteroidsData.postValue(databaseDao.getAll())
        }
    }

    private suspend fun getNewPicOfTheDayUrl() {
        try {
            val result = withContext(Dispatchers.IO) {
                NetworkApi.retrofitService.getApod()
            }
            if(result.mediaType == "image") {
                _apodUrl.postValue(result.url)
                _apodTitle.postValue(result.explanation)
            } else {
                _missingApod.postValue(true)
            }
        } catch(e: Exception) {
            Timber.e(e.message)
            _missingApod.postValue(true)
        }
    }

    fun onAsteroidClicked(id: Long) {
        _navigateToAsteroidDetail.value = id
    }

    fun onAsteroidDetailNavigated() {
        _navigateToAsteroidDetail.value = null
    }

    class Factory(
        private val dataSource: AsteroidDatabaseDao,
        private val application: Application
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(dataSource, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

