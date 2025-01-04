package com.example.registrofumar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pitillos")
data class ListaPitillos (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "tipoProducto") val tipoProducto: String,
    @ColumnInfo(name = "anyoAdquisicion") val anyoAdquisicion: Int,
    @ColumnInfo(name = "mesAdquisicion") val mesAdquisicion: Int,
    @ColumnInfo(name = "diaAdquisicion") val diaAdquisicion: Int,
    @ColumnInfo(name = "importeGastado") val importeGastado: Double
)