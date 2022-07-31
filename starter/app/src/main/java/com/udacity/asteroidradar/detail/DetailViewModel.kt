package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.udacity.asteroidradar.Asteroid

class DetailViewModel(asteroid: Asteroid, application: Application) : ViewModel() {

    private val _selectedAsteroid = MutableLiveData<Asteroid>()

    val selectedProperty: LiveData<Asteroid>
        get() = _selectedAsteroid

    init {
        _selectedAsteroid.value = asteroid
    }

}

class DetailViewModelFactory(
    private val asteroid: Asteroid,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(asteroid, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}