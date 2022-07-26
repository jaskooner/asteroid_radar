package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Room.Companion.databaseBuilder
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: DatabaseAsteroid)

    @Query("SELECT * FROM databaseasteroid WHERE datetime(closeApproachDate) >= datetime() ORDER BY datetime(closeApproachDate)")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = databaseBuilder(context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids").build()
        }
    }
    return INSTANCE
}
