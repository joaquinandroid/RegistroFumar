package com.example.registrofumar

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PitillosRepository @Inject constructor(private val pitillosDao: PitillosDao) {

    val todosPitillos: LiveData<MutableList<ListaPitillos>> = pitillosDao.getTodosPitillos().asLiveData()

    suspend fun borrarTodo(){
        return pitillosDao.borrarTodo()
    }

    suspend fun insert(pitillo: ListaPitillos): Long {
        return pitillosDao.insert(pitillo)
    }

    fun getGastosPorMes(mes: Int): Flow<Double> {
        return pitillosDao.getGastosPorMes(mes)
    }

    fun getGastosPorDia(dia: Int): Flow<Double> {
        return pitillosDao.getGastosPorDia(dia)
    }

    suspend fun getGastosPorMesPitillo(fecha: Int, tipoProducto: String): Flow<Double> {
        return pitillosDao.getGastosPorMesPitillo(fecha, tipoProducto)
    }
}