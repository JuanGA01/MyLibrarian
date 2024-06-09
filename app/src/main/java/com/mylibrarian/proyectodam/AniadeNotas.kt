package com.mylibrarian.proyectodam

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mylibrarian.proyectodam.modelo.LibroViewModelFactory
import com.mylibrarian.proyectodam.modelo.MyViewModel
import com.mylibrarian.proyectodam.persistencia.DbOpenHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AniadeNotas(dbOpenHelper: DbOpenHelper, navController: NavController, libroId: Int, myViewModel: MyViewModel = viewModel(factory = LibroViewModelFactory(dbOpenHelper))){
    // Busca el libro en el ViewModel
    val libro = myViewModel.buscarPorId(libroId)
    // Estado para almacenar y recordar las notas del libro
    var notas by remember { mutableStateOf(TextFieldValue(libro?.notas ?: "")) }
    // Obtiene el contexto actual
    val context = LocalContext.current

    Scaffold(
        topBar = {
            // Barra superior con título y botón de navegación para volver
            TopAppBar(
                title = { Text("Notas del Préstamo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // Botón de acción para guardar las notas
                    IconButton(onClick = {
                        if (libro != null) {
                            libro.notas = notas.text
                            myViewModel.actualizarLibro(libro)
                            navController.popBackStack()
                        } else {
                            // Muestra un mensaje si el libro no se encuentra
                            Toast.makeText(context, "No se encontró el libro", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Filled.Check, contentDescription = "Guardar Notas")
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
            // Muestra la imagen del libro
            libro?.let {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(it.portada)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(240.dp)
                        .width(150.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Campo de texto para editar las notas del libro
            OutlinedTextField(
                value = notas,
                onValueChange = { notas = it },
                label = { Text("Notas") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                maxLines = 10,
                singleLine = false
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
