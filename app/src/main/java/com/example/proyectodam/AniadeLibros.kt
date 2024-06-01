package com.example.proyectodam

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectodam.modelo.Libro
import com.example.proyectodam.modelo.LibroViewModelFactory
import com.example.proyectodam.modelo.MyViewModel
import com.example.proyectodam.persistencia.DbOpenHelper

@Composable
fun AniadeLibros(dbOpenHelper: DbOpenHelper, navController: NavController, myViewModel: MyViewModel = viewModel(factory = LibroViewModelFactory(dbOpenHelper))) {
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
    var estanteria by remember { mutableStateOf(1) }
    var estante by remember { mutableStateOf(1) }
    var seccion by remember { mutableStateOf("") }
    var imagenBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título del libro") },
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
        OutlinedTextField(
            value = resumen,
            onValueChange = { resumen = it },
            label = { Text("Resumen") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = fechaAdquisicion,
            onValueChange = { fechaAdquisicion = it },
            label = { Text("Fecha de Adquisición") },
            modifier = Modifier.fillMaxWidth()
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
                    estanteria = value.coerceIn(1, 100) },
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
                    estante = value.coerceIn(1, 10)},
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
        if (imagenBitmap != null) {
            Image(
                bitmap = imagenBitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        } else {
            val context = LocalContext.current
            val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = {
                        uri -> selectedImageUri = uri
                    val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    context.contentResolver.takePersistableUriPermission(uri!!, flag)
                }
            )
            Column {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                Button(
                    onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    Text(text = "Pick photo")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            myViewModel.addLibro(dbOpenHelper,
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
        }) {
            Text(text = "Añadir Libro")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

}