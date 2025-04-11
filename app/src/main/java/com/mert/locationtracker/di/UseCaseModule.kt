package com.mert.locationtracker.di

import android.content.Context
import com.mert.locationtracker.domain.repository.LocationRepository
import com.mert.locationtracker.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetSavedLocationsUseCase(repository: LocationRepository): GetSavedLocationsUseCase {
        return GetSavedLocationsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideStartLocationTrackingUseCase(context: Context): StartLocationTrackingUseCase {
        return StartLocationTrackingUseCase(context)
    }

    @Provides
    @Singleton
    fun provideStopLocationTrackingUseCase(context: Context): StopLocationTrackingUseCase {
        return StopLocationTrackingUseCase(context)
    }

    @Provides
    @Singleton
    fun provideClearLocationsUseCase(repository: LocationRepository): ClearLocationsUseCase {
        return ClearLocationsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAddressFromLocationUseCase(repository: LocationRepository): GetAddressFromLocationUseCase {
        return GetAddressFromLocationUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddLocationUseCase(repository: LocationRepository): AddLocationUseCase {
        return AddLocationUseCase(repository)
    }
}