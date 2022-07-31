package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NeoApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val dateFormatter = DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT)

            var startDate = LocalDateTime.now()
            val endDate = startDate.plusDays(7)

            val formatedStartDate = startDate.format(dateFormatter)
            val formattedEndDate = endDate.format(dateFormatter)

            val neoJSONOStr = NeoApi.retrofitStringService.getProperties(formatedStartDate, formattedEndDate, Constants.API_KEY)
            val neoJsonObject = JSONObject(neoJSONOStr)
            val asteroidsArrayList = parseAsteroidsJsonResult(neoJsonObject)

            database.asteroidDao.insertAll(*asteroidsArrayList.asDatabaseModel())

        }
    }
}