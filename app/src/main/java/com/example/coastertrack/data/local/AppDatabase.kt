package com.example.coastertrack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.coastertrack.data.model.room.LocalParkEntity
import com.example.coastertrack.data.model.room.LocalRollercoasterEntity
import com.example.coastertrack.data.model.room.LocalVisitEntity

// creates a room database from, the provided entities
@Database(
    entities = [LocalRollercoasterEntity::class, LocalParkEntity::class, LocalVisitEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rollercoasterDao(): RollercoasterDao
    abstract fun parkDao(): ParkDao
    abstract fun visitDao(): VisitDao


}