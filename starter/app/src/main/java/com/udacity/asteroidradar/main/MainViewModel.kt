package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NeoApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainViewModel : ViewModel() {

    private val TAG = ViewModel::class.qualifiedName


    // The internal MutableLiveData String that stores the most recent property response
    private val _propResponse = MutableLiveData<ArrayList<Asteroid>>()

    // Internal status of asteroid json response
    private val _statusNeo = MutableLiveData<Status>()
    // External immutable LiveData for the asteroid response status
    val statusNeo: LiveData<Status>
        get() = _statusNeo

    // The external immutable LiveData for the property response String
    val propResponse: LiveData<ArrayList<Asteroid>>
        get() = _propResponse


    // Internal status of image of the day reponse
    private val _statusImg = MutableLiveData<Status>()
    // External immutable live date for the status of the image of the day respone
    val statusImg : LiveData<Status>
        get() = _statusImg

    // The internal MutableLiveData String that stores the Image of the Day response
    private val _pictureOfDay = MutableLiveData<PictureOfDay>()

    // The external immutable LiveData for the Image of the Day response
    val picOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    /**
     * Call getNeoProperties() on init so we can display status immediately.
     * CALL GetImgOfTheDay() on init so we can display image immediately
     */
    init {
        _statusNeo.value = Status.NOT_STARTED
        getNeoProperties()
        getPictureOfTheDay()
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

        NeoApi.retrofitStringService.getProperties(formatedStartDate, formattedEndDate, Constants.API_KEY)
            .enqueue(object: Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    // convert json string to json object
                    val neoJsonObject = JSONObject(response.body())
                    // get the Neo as Asteroid Object ArrayList from the json object
                    _propResponse.value = parseAsteroidsJsonResult(neoJsonObject)
                    // set the success status
                    _statusNeo.value = Status.SUCCESS
                    Log.i(TAG, "getProperties(): ${_propResponse.value}")
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    // set the status
                    _statusNeo.value = Status.FAILURE
                    Log.e(TAG, "getProperties failure: ${t.message}")
                }
            })
    }

    /**
     * Set the value of the image of the day response LiveData to the Nasa Image of the day.
     */
    private fun getPictureOfTheDay() {
        NeoApi.retrofitPicService.getImageOfTheDay(Constants.API_KEY)
            .enqueue(object: Callback<PictureOfDay>{
                override fun onResponse(call: Call<PictureOfDay>, response: Response<PictureOfDay>) {
                    _pictureOfDay.value = response.body()
                    // set status
                    _statusImg.value = Status.SUCCESS
                    Log.i(TAG, "getImageOfTheDay Success: ${response.body()}")
                }

                override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                    // set status
                    _statusImg.value = Status.FAILURE
                    Log.e(TAG, "getImageOfTheDay Failure: ${t.message}")
                }
            })
    }


}

enum class Status {
    NOT_STARTED, SUCCESS, FAILURE
}