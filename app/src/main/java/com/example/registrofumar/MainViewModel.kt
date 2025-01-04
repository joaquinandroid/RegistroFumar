package com.example.registrofumar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: PitillosRepository) : ViewModel() {

    private val _todosPitillos = MutableLiveData<MutableList<ListaPitillos>>()
    val todosPitillos: LiveData<MutableList<ListaPitillos>> get() = _todosPitillos

    private val _gastosMes = MutableLiveData<Double?>(0.0)
    val gastosMes: LiveData<Double?> get() = _gastosMes

    private val _gastosPorMesPitillo = MutableLiveData<Double>()
    val gastosPorMesPitillo: LiveData<Double> get() = _gastosPorMesPitillo

    val tiposDeProducto: List<String> = listOf(
        "Normal", "De liar", "Otros"
    )

    init {
        _todosPitillos.value = repository.todosPitillos.value ?: mutableListOf()
    }

    fun getRepository(): PitillosRepository {
        return repository
    }

    suspend fun borrarTodo(){
        return repository.borrarTodo()
    }

    fun insertPitillo(pitillo: ListaPitillos) {
        viewModelScope.launch(Dispatchers.IO) {
            val nuevoPitillo = repository.insert(pitillo)
        }
    }

    suspend fun getGastosPorMes(mes: Int): LiveData<Double?> {
        return repository.getGastosPorMes(mes)
            .catch { exception ->
                // Manejar la excepción, por ejemplo, loguearla o mostrar un mensaje de error
                Log.e("Error", "Error al obtener los gastos por mes: ${exception.message}")
            }
            .asLiveData()
    }

    suspend fun getGastosPorDia(dia: Int): LiveData<Double?> {
        return repository.getGastosPorDia(dia)
            .catch { exception ->
                // Manejar la excepción, por ejemplo, loguearla o mostrar un mensaje de error
                Log.e("Error", "Error al obtener los gastos por mes: ${exception.message}")
            }
            .asLiveData()
    }

    suspend fun getGastosPorMesPitillo(mes: Int, tipoPitillo: String): LiveData<Double> {
        return repository.getGastosPorMesPitillo(mes, tipoPitillo)
            .catch { exception ->
                // Manejar la excepción, por ejemplo, loguearla o mostrar un mensaje de error
                Log.e("Error", "Error al obtener los gastos por mes pitillo: ${exception.message}")
            }
            .asLiveData()
    }
}