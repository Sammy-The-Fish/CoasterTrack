package com.example.coastertrack

import android.content.Context
import androidx.room.Room
import com.example.coastertrack.data.local.AppDatabase
import com.example.coastertrack.data.local.ParkDao
import com.example.coastertrack.data.local.RollercoasterDao
import com.example.coastertrack.data.local.VisitDao
import com.example.coastertrack.data.repository.ParkDatabaseRepository
import com.example.coastertrack.data.repository.RollercoasterDatabaseRepository
import com.example.coastertrack.data.repository.VisitDatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// object handling dependency injection between view models and data layer

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun providesRollercoasterDatabase(@ApplicationContext app: Context): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "rollercoaster_database___"
        )
            .build()
    }

    @Provides
    fun providesRollercoasterDao(database: AppDatabase): RollercoasterDao {
        return database.rollercoasterDao()
    }

    @Provides
    fun providesParkDao(database: AppDatabase): ParkDao {
        return database.parkDao()
    }

    @Provides
    fun providesRollercoasterDatabaseRepository(dao: RollercoasterDao): RollercoasterDatabaseRepository {
        return RollercoasterDatabaseRepository(dao)
    }

    @Provides
    fun providesVisitDao(database: AppDatabase): VisitDao {
        return database.visitDao()
    }

    @Provides
    fun providesParkDatabaseRepository(dao: ParkDao): ParkDatabaseRepository {
        return ParkDatabaseRepository(dao)
    }


    @Provides
    fun providesVisitDatabaseRepository(dao: VisitDao): VisitDatabaseRepository {
        return VisitDatabaseRepository(dao)
    }
}