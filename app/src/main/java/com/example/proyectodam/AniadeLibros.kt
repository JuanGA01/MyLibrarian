package com.example.proyectodam

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.proyectodam.modelo.Libro
import com.example.proyectodam.modelo.LibroViewModelFactory
import com.example.proyectodam.modelo.MyViewModel
import com.example.proyectodam.persistencia.DbOpenHelper
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AniadeLibros(
    dbOpenHelper: DbOpenHelper,
    navController: NavController,
    myViewModel: MyViewModel = viewModel(factory = LibroViewModelFactory(dbOpenHelper))
) {
    var titulo by remember { mutableStateOf(TextFieldValue()) }
    var isbn by remember { mutableStateOf(TextFieldValue()) }
    var autor by remember { mutableStateOf(TextFieldValue()) }
    var editorial by remember { mutableStateOf(TextFieldValue()) }
    var anioPublicacion by remember { mutableStateOf(TextFieldValue()) }
    var genero by remember { mutableStateOf(TextFieldValue()) }
    var numeroPaginas by remember { mutableStateOf(TextFieldValue()) }
    var idioma by remember { mutableStateOf(TextFieldValue()) }
    var resumen by remember { mutableStateOf(TextFieldValue()) }
    var fechaAdquisicion by remember { mutableStateOf(TextFieldValue()) }
    var estanteria by remember { mutableIntStateOf(1) }
    var estante by remember { mutableIntStateOf(1) }
    var seccion by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir Libro") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (titulo.text.isNotEmpty() && selectedImageUri != null) {
                            myViewModel.addLibro(
                                dbOpenHelper,
                                Libro(
                                    titulo = titulo.text,
                                    isbn = isbn.text,
                                    autor = autor.text,
                                    editorial = editorial.text,
                                    anioPublicacion = anioPublicacion.text.toIntOrNull() ?: 0,
                                    genero = genero.text,
                                    numeroPaginas = numeroPaginas.text.toIntOrNull() ?: 0,
                                    idioma = idioma.text,
                                    resumen = resumen.text,
                                    fechaAdquisicion = fechaAdquisicion.text,
                                    estanteria = estanteria,
                                    estante = estante,
                                    seccion = seccion.firstOrNull() ?: ' ',
                                    portada = selectedImageUri.toString()
                                )
                            )
                            navController.popBackStack()
                        } else {
                            val message = if (titulo.text.isEmpty()) {
                                "El título es obligatorio"
                            } else {
                                "Debe subir una portada"
                            }
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Filled.Check, contentDescription = "Guardar Libro")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("*Título del libro") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = isbn,
                onValueChange = { isbn = it },
                label = { Text("ISBN") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = autor,
                onValueChange = { autor = it },
                label = { Text("Autor") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = editorial,
                onValueChange = { editorial = it },
                label = { Text("Editorial") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = anioPublicacion,
                onValueChange = { anioPublicacion = it },
                label = { Text("Año de Publicación") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = genero,
                onValueChange = { genero = it },
                label = { Text("Género") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = numeroPaginas,
                onValueChange = { numeroPaginas = it },
                label = { Text("Número de Páginas") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = idioma,
                onValueChange = { idioma = it },
                label = { Text("Idioma") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))










            val datePickerDialog = DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    calendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
                    fechaAdquisicion = TextFieldValue(dateFormatter.format(calendar.time))
                }, year, month, day
            )
            Button(onClick = { datePickerDialog.show() }) {
                Text(text = "Select Date")
            }
            OutlinedTextField(
                value = fechaAdquisicion,
                onValueChange = { fechaAdquisicion = it },
                label = { Text("Fecha de Adquisición") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        datePickerDialog.show()
                    }
            )
            Spacer(modifier = Modifier.height(16.dp))





            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = estanteria.toString(),
                    onValueChange = {
                        val value = it.toIntOrNull() ?: 0
                        estanteria = value.coerceIn(1, 100)
                    },
                    label = { Text("Estantería") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = estante.toString(),
                    onValueChange = {
                        val value = it.toIntOrNull() ?: 0
                        estante = value.coerceIn(1, 10)
                    },
                    label = { Text("Estante") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
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
                        .padding(horizontal = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = resumen,
                onValueChange = { resumen = it },
                label = { Text("Resumen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 10,
                singleLine = false
            )
            Spacer(modifier = Modifier.height(16.dp))
            val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = { uri ->
                    selectedImageUri = uri
                    uri?.let {
                        context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                }
            )
            Button(
                onClick = {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            ) {
                Text(text = "*Elige la portada de tu libro")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                selectedImageUri?.let {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(it)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .height(200.dp)
                            .width(150.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}