package com.example.registrofumar

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ListaPitillos::class], version = 1)
abstract class PitillosDatabase: RoomDatabase() {
    abstract fun pitillosDao(): PitillosDao
}