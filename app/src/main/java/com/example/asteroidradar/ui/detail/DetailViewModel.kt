package com.example.asteroidradar.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.asteroidradar.network_database.database.AsteroidData
import com.example.asteroidradar.network_database.database.AsteroidDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(
    asteroidId: Long,
    databaseDao: AsteroidDatabaseDao,
    application: Application
) : AndroidViewModel(application) {
    private val _asteroidData = MutableLiveData<AsteroidData?>()
    val asteroidData: LiveData<AsteroidData?>
        get() = _asteroidData

    init {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                databaseDao.get(asteroidId)
            }
            _asteroidData.postValue(result)
        }
    }
}

class DetailViewModelFactory(
    private val asteroidId: Long,
    private val databaseDao: AsteroidDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(asteroidId, databaseDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}