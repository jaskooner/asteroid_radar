package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NeoApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainViewModel : ViewModel() {

    private val TAG = ViewModel::class.qualifiedName

    // The internal MutableLiveData String that stores the most recent property response
    private val _propResponse = MutableLiveData<String>()

    // The external immutable LiveData for the property response String
    val propResponse: LiveData<String>
        get() = _propResponse

    // The internal MutableLiveData String that stores the Image of the Day response
    private val _imgOfTheDayResponse = MutableLiveData<String>()

    // The external immutable LiveData for the Image of the Day response
    val imgOfTheDayResponse: LiveData<String>
        get() = _imgOfTheDayResponse

    /**
     * Call getNeoProperties() on init so we can display status immediately.
     * CALL GetImgOfTheDay() on init so we can display image immediately
     */
    init {
        getNeoProperties()
        getImgOfTheDay()
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

        NeoApi.retrofitService.getProperties(formatedStartDate, formattedEndDate, Constants.API_KEY)
            .enqueue(object: Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    _propResponse.value = response.body()
                    Log.i(TAG, "getProperties: ${response.body()}")
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    _propResponse.value = "Failure: ${t.message}"
                    Log.e(TAG, "getProperties failure: ${t.message}")
                }
            })
    }

    /**
     * Set the value of the image of the day response LiveData to the Nasa Image of the day.
     */
    private fun getImgOfTheDay() {
        NeoApi.retrofitService.getImageOfTheDay(Constants.API_KEY)
            .enqueue(object: Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    _imgOfTheDayResponse.value = response.body()
                    Log.i(TAG, "getImageOfTheDay Success: ${response.body()}")
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    _imgOfTheDayResponse.value = "Failure: ${t.message}"
                    Log.e(TAG, "getImageOfTheDay Failure: ${t.message}")
                }
            })
    }


}