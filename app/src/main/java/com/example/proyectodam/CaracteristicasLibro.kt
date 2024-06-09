package com.example.proyectodam

import android.app.DatePickerDialog
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectodam.modelo.LibroViewModelFactory
import com.example.proyectodam.modelo.MyViewModel
import com.example.proyectodam.persistencia.DbOpenHelper
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaracteristicasLibro(
    dbOpenHelper: DbOpenHelper,
    navController: NavController,
    libroId: Int,
    myViewModel: MyViewModel = viewModel(factory = LibroViewModelFactory(dbOpenHelper))
) {
    // Obtiene el libro a partir del ID proporcionado
    val libro = myViewModel.buscarPorId(libroId)

    //Variables relacionadas al DatePicker
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // Si el libro existe, muestra sus detalles
    libro?.let {
        // Variables de estado para cada campo del libro
        var titulo by remember { mutableStateOf(libro.titulo) }
        var autor by remember { mutableStateOf(libro.autor ?: "") }
        var resumen by remember { mutableStateOf(libro.resumen ?: "") }
        var isbn by remember { mutableStateOf(libro.isbn ?: "") }
        var editorial by remember { mutableStateOf(libro.editorial ?: "") }
        var anioPublicacion by remember { mutableStateOf(libro.anioPublicacion ?: 0) }
        var genero by remember { mutableStateOf(libro.genero ?: "") }
        var numeroPaginas by remember { mutableStateOf(libro.numeroPaginas ?: 0) }
        var idioma by remember { mutableStateOf(libro.idioma ?: "") }
        var fechaAdquisicion by remember { mutableStateOf(libro.fechaAdquisicion ?: "") }
        var estanteria by remember { mutableStateOf(libro.estanteria) }
        var estante by remember { mutableStateOf(libro.estante) }
        var seccion by remember { mutableStateOf(libro.seccion.toString()) }

        // Variables relacionadas al DatePicker
        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                calendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
                fechaAdquisicion = TextFieldValue(dateFormatter.format(calendar.time)).text
            }, year, month, day
        )

        // Fuente de interacción para mostrar el diálogo del selector de fecha al hacer clic
        val interactionSource = remember {
            object : MutableInteractionSource {
                override val interactions = MutableSharedFlow<Interaction>(
                    extraBufferCapacity = 16,
                    onBufferOverflow = BufferOverflow.DROP_OLDEST,
                )
                override suspend fun emit(interaction: Interaction) {
                    if (interaction is PressInteraction.Release) {
                        datePickerDialog.show()
                    }
                    interactions.emit(interaction)
                }
                override fun tryEmit(interaction: Interaction): Boolean {
                    return interactions.tryEmit(interaction)
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Detalles del Libro") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate("TabScreen") }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                        }
                    },
                    actions = {
                        Button(
                            onClick = {
                                myViewModel.cambiarEstado(libro)
                                navController.navigate("CaracteristicasLibro/${libro.id}")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (libro.prestado) Color.Red else Color.Blue,
                                contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (libro.prestado) {
                                Text(text = "Devolver")
                            } else {
                                Text(text = "Prestar")
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    libro.portada?.let { uri ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center,
                                modifier = Modifier
                                    .height(240.dp)
                                    .width(150.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = titulo,
                        onValueChange = { titulo = it },
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = isbn,
                        onValueChange = { isbn = it },
                        label = { Text("ISBN") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = autor,
                        onValueChange = { autor = it },
                        label = { Text("Autor") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editorial,
                        onValueChange = { editorial = it },
                        label = { Text("Editorial") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = anioPublicacion.toString(),
                        onValueChange = { anioPublicacion = it.toIntOrNull() ?: 0 },
                        label = { Text("Año de publicación") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = genero,
                        onValueChange = { genero = it },
                        label = { Text("Género") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = numeroPaginas.toString(),
                        onValueChange = { numeroPaginas = it.toIntOrNull() ?: 0 },
                        label = { Text("Número de páginas") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = idioma,
                        onValueChange = { idioma = it },
                        label = { Text("Idioma") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = fechaAdquisicion,
                        onValueChange = { fechaAdquisicion = it },
                        label = { Text("Fecha de Adquisición") },
                        readOnly = true,
                        interactionSource = interactionSource,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ){
                        OutlinedTextField(
                            value = estanteria.toString(),
                            onValueChange = { estanteria = it.toIntOrNull()?.coerceIn(1, 100) ?: 1 },
                            label = { Text("Estantería") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = estante.toString(),
                            onValueChange = { estante = it.toIntOrNull()?.coerceIn(1, 20) ?: 1 },
                            label = { Text("Estante") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = seccion,
                            onValueChange = { newValue ->
                                if (newValue.length <= 1 && newValue.all { it.isLetter() }) {
                                    seccion = newValue.uppercase()
                                }
                            },
                            label = { Text("Sección") },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.Characters
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused && seccion.equals(" ")) {
                                        seccion = ""
                                    }
                                }
                        )
                    }
                    OutlinedTextField(
                        value = resumen,
                        onValueChange = { resumen = it },
                        label = { Text("Resumen") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4,
                        singleLine = false
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                //Comprobamos que sección no esté vacío
                                if (seccion.length > 0){
                                    seccion.toCharArray()[0]
                                } else{
                                    seccion = " "
                                }
                                // Actualizar libro
                                myViewModel.actualizarLibro(libro.copy(
                                    titulo = titulo,
                                    autor = autor,
                                    resumen = resumen,
                                    isbn = isbn,
                                    editorial = editorial,
                                    anioPublicacion = anioPublicacion,
                                    genero = genero,
                                    numeroPaginas = numeroPaginas,
                                    idioma = idioma,
                                    fechaAdquisicion = fechaAdquisicion,
                                    estanteria = estanteria,
                                    estante = estante,
                                    seccion = seccion.toCharArray()[0]
                                ))
                                navController.navigate("TabScreen")
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Green
                            )
                        ) {
                            Text("Guardar")
                        }
                        Button(
                            onClick = {
                                // Eliminar libro, si se borra se vuelve al menú, si no el botón no hace nada
                                if (myViewModel.borrarPorId(libroId)) {
                                    navController.navigate("TabScreen")
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            )
                        ) {
                            Text("Eliminar", color = Color.White)
                        }
                    }
                }
            }
        }
    } ?: run {
        // En caso de que no se encuentre el libro, cosa en teoría imposible, manejaremos la excepción así
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Detalles del Libro") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate("TabScreen") }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Libro Borrado")
            }
        }
    }
}
