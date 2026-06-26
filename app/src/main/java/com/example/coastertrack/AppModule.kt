package com.example.coastertrack

import android.content.Context
import com.example.coastertrack.data.repository.QueueTimeRepository
import com.example.coastertrack.data.repository.RcdbRepository
import com.example.coastertrack.data.repository.UserPreferencesRepository
import com.example.coastertrack.domain.usecases.GetRollercoasterDetailsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// object handling dependency injection between view models and data layer
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideQueueTimeRepository(): QueueTimeRepository {
        return QueueTimeRepository()
    }

    @Provides
    fun provideRcdbRepository(): RcdbRepository {
        return RcdbRepository()
    }

    @Provides
    fun providesRollercoasterDetailsUseCase(repository: RcdbRepository): GetRollercoasterDetailsUseCase {
        return GetRollercoasterDetailsUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesUserReferencesRepository(
        @ApplicationContext context: Context
    ): UserPreferencesRepository {
        return UserPreferencesRepository(context)
    }
}