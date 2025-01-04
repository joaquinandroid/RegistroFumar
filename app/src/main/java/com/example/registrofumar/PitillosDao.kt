package com.example.registrofumar

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PitillosDao {

    @Query("SELECT * FROM pitillos")
    fun getTodosPitillos(): Flow<MutableList<ListaPitillos>>

    @Query("DELETE FROM pitillos")
    fun borrarTodo()

    @Insert
    fun insert(pitillo: ListaPitillos): Long

    @Query("SELECT SUM(importeGastado) FROM pitillos WHERE mesAdquisicion = :mes")
    fun getGastosPorMes(mes: Int): Flow<Double>

    @Query("SELECT SUM(importeGastado) FROM pitillos WHERE diaAdquisicion = :dia")
    fun getGastosPorDia(dia: Int): Flow<Double>

    @Query("SELECT SUM(importeGastado) FROM pitillos WHERE mesAdquisicion = :mes AND tipoProducto = :tipo")
    fun getGastosPorMesPitillo(mes: Int, tipo: String): Flow<Double>
}