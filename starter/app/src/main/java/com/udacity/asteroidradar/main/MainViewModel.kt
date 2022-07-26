package com.udacity.asteroidradar.main

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

    // The internal MutableLiveData String that stores the most recent response
    val _response = MutableLiveData<String>()

    // The external immutable LiveData for the response String
    val response: LiveData<String>
        get() = _response

    /**
     * Call getNeoProperties() on init so we can display status immediately.
     */
    init {
        getNeoProperties()
    }

    /**
     * Sets the value of the response LiveData to the Nasa Near Earth Object API status or the successful number of
     * Nasa Near Earth Objects retrieved.
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
                    _response.value = response.body()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    _response.value = "Failure: ${t.message}"
                }
            })

    }


}