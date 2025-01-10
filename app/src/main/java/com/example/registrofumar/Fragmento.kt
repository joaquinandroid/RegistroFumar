package com.example.registrofumar

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class Fragmento : Fragment() {

    private lateinit var viewModel: MainViewModel

    lateinit var txtCuentaCigarrillos: TextView
    lateinit var txtHoraCigarrilloPasado: TextView

    private lateinit var sharedPreferences: SharedPreferences
    private val SHARED_PREFS_NAME = "myPrefs"
    private val COUNT_KEY = "count"
    private val HORA_KEY = "hour"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragmento, container, false)

        txtCuentaCigarrillos = view?.findViewById<TextView>(R.id.txtCuentaCigarrillos)!!
        val btnSumarCigarrillos = view?.findViewById<Button>(R.id.btnSumarCigarrillos)
        val btnResetCigarrillos = view?.findViewById<Button>(R.id.btnResetCigarrillos)
        txtHoraCigarrilloPasado = view?.findViewById<TextView>(R.id.txtHoraCigarrilloPasado)!!

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)

        // Cargar el valor guardado en SharedPreferences
        var count = sharedPreferences.getInt(COUNT_KEY, 0)
        var hora = sharedPreferences.getString(HORA_KEY, "00:00")
        txtCuentaCigarrillos.text = count.toString()
        txtHoraCigarrilloPasado.text = hora

        btnSumarCigarrillos?.setOnClickListener {
            val horaActual = LocalTime.now()
            count++
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val horaSinSegundos = horaActual?.format(formatter)

            with(sharedPreferences.edit()) {
                putInt(COUNT_KEY, count)
                putString(HORA_KEY, horaSinSegundos)
                apply()
            }

        }

        btnResetCigarrillos?.setOnClickListener {
            // Reiniciar el contador a 0 y actualizar el TextView
            count = 0
            txtCuentaCigarrillos?.text = ""
            // Guardar el nuevo valor en SharedPreferences
            with(sharedPreferences.edit()) {
                putInt(COUNT_KEY, count)
                apply()
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val txtEnero = view?.findViewById<TextView>(R.id.txtEnero)
        val txtFebrero = view?.findViewById<TextView>(R.id.txtFebrero)
        val txtMarzo = view?.findViewById<TextView>(R.id.txtMarzo)
        val txtAbril = view?.findViewById<TextView>(R.id.txtAbril)
        val txtMayo = view?.findViewById<TextView>(R.id.txtMayo)
        val txtJunio = view?.findViewById<TextView>(R.id.txtJunio)
        val txtJulio = view?.findViewById<TextView>(R.id.txtJulio)
        val txtAgosto = view?.findViewById<TextView>(R.id.txtAgosto)
        val txtSeptiembre = view?.findViewById<TextView>(R.id.txtSeptiembre)
        val txtOctubre = view?.findViewById<TextView>(R.id.txtOctubre)
        val txtNoviembre = view?.findViewById<TextView>(R.id.txtNoviembre)
        val txtDiciembre = view?.findViewById<TextView>(R.id.txtDic)
        val txtTipo = view?.findViewById<AutoCompleteTextView>(R.id.txtTipo)
        val edtImporte = view?.findViewById<EditText>(R.id.edtImporte)
        val btnAnyadir = view?.findViewById<Button>(R.id.btnAnyadir)
        val txtDeLiar = view?.findViewById<TextView>(R.id.txtDeLiar)
        val txtNormal = view?.findViewById<TextView>(R.id.txtNormal)
        val txtOtros = view?.findViewById<TextView>(R.id.txtOtros)
        val txtHoy = view?.findViewById<TextView>(R.id.txtHoy)
        val txtMes = view?.findViewById<TextView>(R.id.txtMes)
        val btnSalir = view?.findViewById<Button>(R.id.btnSalir)
        val btnReiniciar = view?.findViewById<Button>(R.id.btnReiniciar)

        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_dropdown_item_1line,
            viewModel.tiposDeProducto
        )
        if (txtTipo != null) {
            txtTipo.setAdapter(adapter)
        }

        if (btnAnyadir != null) {
            btnAnyadir.setOnClickListener {
                try {

                    val fecha: LocalDate = LocalDate.now()
                    val tipo: String = txtTipo?.text.toString()
                    val anyo: Int = fecha.year
                    val mes: Int = fecha.monthValue
                    val dia: Int = fecha.dayOfMonth
                    val importe: Double = edtImporte?.text.toString().toDouble()
                    val nuevoPitillo: ListaPitillos =
                        ListaPitillos(0, tipo, anyo, mes, dia, importe)

                    if ((nuevoPitillo.tipoProducto == "Normal" ||
                                nuevoPitillo.tipoProducto == "De liar" ||
                                nuevoPitillo.tipoProducto == "Otros")
                    ) {

                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                            viewModel.getRepository().insert(nuevoPitillo)
                        }

                        Thread {
                            viewLifecycleOwner.lifecycleScope.launch {
                                // Obtener los gastos para cada mes en una sola llamada
                                val gastosEnero =
                                    viewModel.getGastosPorMes(1)!!.asFlow().first() ?: 0.0
                                val gastosFebrero =
                                    viewModel.getGastosPorMes(2)!!.asFlow().first() ?: 0.0
                                val gastosMarzo =
                                    viewModel.getGastosPorMes(3)!!.asFlow().first() ?: 0.0
                                val gastosAbril =
                                    viewModel.getGastosPorMes(4)!!.asFlow().first() ?: 0.0
                                val gastosMayo =
                                    viewModel.getGastosPorMes(5)!!.asFlow().first() ?: 0.0
                                val gastosJunio =
                                    viewModel.getGastosPorMes(6)!!.asFlow().first() ?: 0.0
                                val gastosJulio =
                                    viewModel.getGastosPorMes(7)!!.asFlow().first() ?: 0.0
                                val gastosAgosto =
                                    viewModel.getGastosPorMes(8)!!.asFlow().first() ?: 0.0
                                val gastosSeptiembre =
                                    viewModel.getGastosPorMes(9)!!.asFlow().first() ?: 0.0
                                val gastosOctubre =
                                    viewModel.getGastosPorMes(10)!!.asFlow().first() ?: 0.0
                                val gastosNoviembre =
                                    viewModel.getGastosPorMes(11)!!.asFlow().first() ?: 0.0
                                val gastosDiciembre =
                                    viewModel.getGastosPorMes(12)!!.asFlow().first() ?: 0.0
                                val gastosNormal =
                                    viewModel.getGastosPorMesPitillo(mes, "Normal").asFlow()
                                        .firstOrNull() ?: 0.0
                                val gastosDeLiar =
                                    viewModel.getGastosPorMesPitillo(mes, "De liar").asFlow()
                                        .firstOrNull() ?: 0.0
                                val gastosOtros =
                                    viewModel.getGastosPorMesPitillo(mes, "Otros").asFlow()
                                        .firstOrNull() ?: 0.0
                                val gastoHoy =
                                    viewModel.getGastosPorDia(dia)!!.asFlow().first() ?: 0.0
                                val gastoMes =
                                    viewModel.getGastosPorMes(mes)!!.asFlow().first() ?: 0.0

                                // Actualizar los TextViews en el hilo principal
                                withContext(Dispatchers.Main) {
                                    txtEnero?.text = gastosEnero.toString()
                                    txtFebrero?.text = gastosFebrero.toString()
                                    txtMarzo?.text = gastosMarzo.toString()
                                    txtAbril?.text = gastosAbril.toString()
                                    txtMayo?.text = gastosMayo.toString()
                                    txtJunio?.text = gastosJunio.toString()
                                    txtJulio?.text = gastosJulio.toString()
                                    txtAgosto?.text = gastosAgosto.toString()
                                    txtSeptiembre?.text = gastosSeptiembre.toString()
                                    txtOctubre?.text = gastosOctubre.toString()
                                    txtNoviembre?.text = gastosNoviembre.toString()
                                    txtDiciembre?.text = gastosDiciembre.toString()
                                    txtNormal?.text = gastosNormal.toString()
                                    txtDeLiar?.text = gastosDeLiar.toString()
                                    txtOtros?.text = gastosOtros.toString()
                                    txtHoy?.text = gastoHoy.toString()
                                    txtMes?.text = gastoMes.toString()
                                }
                            }
                        }.start()
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Guardado")
                            .setMessage("")
                            .setPositiveButton("Ok") { dialog, which ->
                                // Realizar la acción
                            }
                            .show()
                    } else {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Valores incorrectos")
                            .setMessage("")
                            .setPositiveButton("Ok") { dialog, which ->
                                // Realizar la acción
                            }
                            .show()
                        return@setOnClickListener
                    }
                    // Toast.makeText(requireContext(), "Guardado", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Valores incorrectos")
                        .setMessage("")
                        .setPositiveButton("Ok") { dialog, which ->
                            // Realizar la acción
                        }
                        .show()
                    //Toast.makeText(requireContext(), "Valores incorrectos", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
        }

        if (btnSalir != null) {
            btnSalir.setOnClickListener {
                requireActivity().finish()
            }
        }

        if (btnReiniciar != null) {
            btnReiniciar.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Mensaje importante")
                    .setMessage("¿Estás seguro de continuar y borrar todos los registros?")
                    .setPositiveButton("Sí") { dialog, which ->
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                            viewModel.borrarTodo()
                            // Código a ejecutar después de que se complete la operación de borrado
                        }
                    }
                    .setNegativeButton("No") { dialog, which ->

                    }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}