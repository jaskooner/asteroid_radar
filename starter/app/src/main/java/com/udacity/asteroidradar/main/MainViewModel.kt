package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NeoApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainViewModel : ViewModel() {

    private val TAG = ViewModel::class.qualifiedName


    // The internal MutableLiveData String that stores the most recent property response
    private val _propResponse = MutableLiveData<List<Asteroid>>()
    // The external immutable LiveData for the property response String
    val propResponse: LiveData<List<Asteroid>>
        get() = _propResponse


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
        getNeoProperties()
        getPictureOfTheDay()
    }

    // Functions to navigate to asteroid details
    fun displayToAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }


    /**
     * Sets the value of the property response LiveData to the Nasa Near Earth Object API status
     */
    private fun getNeoProperties() {

        val dateFormatter = DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT)

        var startDate = LocalDateTime.now()
        val endDate = startDate.plusDays(7)

        val formatedStartDate = startDate.format(dateFormatter)
        val formattedEndDate = endDate.format(dateFormatter)

        viewModelScope.launch {
            try {
                val neoJSONOStr = NeoApi.retrofitStringService.getProperties(formatedStartDate, formattedEndDate, Constants.API_KEY)
                val neoJsonObject = JSONObject(neoJSONOStr)
                _propResponse.value = parseAsteroidsJsonResult(neoJsonObject)
                _statusNeo.value = NeoStatus.SUCCESS

                Log.i(TAG, "getProperties(): ${_propResponse.value}")
            } catch (e: Exception) {
                _statusNeo.value = NeoStatus.FAILURE
                _propResponse.value = mutableListOf<Asteroid>()

                Log.e(TAG, "getProperties() failure: ${e.message}")
            }
        }


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