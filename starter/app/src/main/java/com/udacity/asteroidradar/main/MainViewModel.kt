package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NeoApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainViewModel(app: Application) : ViewModel() {

    private val TAG = ViewModel::class.qualifiedName

    private val database = getDatabase(app)
    private val asteroidRepository = AsteroidsRepository(database)

    val propResponse = asteroidRepository.asteroids

    // Internal status of asteroid json response
    private val _statusNeo = MutableLiveData<NeoStatus>()
    // External immutable LiveData for the asteroid response status
    val statusNeo: LiveData<NeoStatus>
        get() = _statusNeo



    // Internal status of image of the day reponse
    private val _statusImg = MutableLiveData<NeoStatus>()
    // External immutable live date for the status of the image of the day respone
    val statusImg : LiveData<NeoStatus>
        get() = _statusImg

    // The internal MutableLiveData String that stores the Image of the Day response
    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    // The external immutable LiveData for the Image of the Day response
    val picOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    // Navigate to selected asteroid detail screen
    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()

    val navigateToSelectedAsteroid : LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    /**
     * Call getNeoProperties() on init so we can display status immediately.
     * CALL GetImgOfTheDay() on init so we can display image immediately
     */
    init {
        _statusNeo.value = NeoStatus.NOT_STARTED
        getPictureOfTheDay()

        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
    }

    // Functions to navigate to asteroid details
    fun displayToAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }




    /**
     * Set the value of the image of the day response LiveData to the Nasa Image of the day.
     */
    private fun getPictureOfTheDay() {

        viewModelScope.launch {
            try {
                _pictureOfDay.value = NeoApi.retrofitPicService.getImageOfTheDay(Constants.API_KEY)
                _statusImg.value = NeoStatus.SUCCESS
                Log.i(TAG, "getPictureOfTheDay() Success: ${_pictureOfDay.value}")
            } catch (e: Exception) {
                _statusImg.value = NeoStatus.FAILURE
                _pictureOfDay.value = null
                Log.e(TAG, "getImageOfTheDay Failure: ${e.message}")
            }
        }


    }


}

enum class NeoStatus {
    NOT_STARTED, SUCCESS, FAILURE
}

/**
 * Factory for constructing MainViewModel with parameter
 */
class MainViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}
