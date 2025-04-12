package com.mert.locationtracker.di

import android.app.Application
import androidx.room.Room
import com.mert.locationtracker.data.local.database.LocationDatabase
import com.mert.locationtracker.data.remote.api.GeocodingService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocationDatabase(app: Application): LocationDatabase {
        return Room.databaseBuilder(
            app,
            LocationDatabase::class.java,
            "location_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocationDao(db: LocationDatabase) = db.locationDao()

    @Provides
    @Singleton
    fun provideGeocodingService(): GeocodingService {
        return Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocodingService::class.java)
    }
}