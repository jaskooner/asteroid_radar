package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit_pic= Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

interface PicApiService {
    @GET(Constants.IMG_DAY)
    fun getImageOfTheDay(@Query("api_key") apiKey: String) : Call<PictureOfDay>
}

object PicApi {
    val retrofitPicService : PicApiService by lazy {
        retrofit_pic.create(PicApiService::class.java)
    }
}

private val retrofit_str = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()

interface NeoApiService {
    @GET(Constants.NEO_PATH)
    fun getProperties(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String) :
        Call<String>
}

object NeoApi {
    val retrofitStringService : NeoApiService by lazy {
        retrofit_str.create(NeoApiService::class.java)
    }
}


