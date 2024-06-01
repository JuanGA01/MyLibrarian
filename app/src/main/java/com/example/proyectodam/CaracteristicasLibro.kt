package com.example.proyectodam

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectodam.modelo.Libro
import com.example.proyectodam.modelo.LibroViewModelFactory
import com.example.proyectodam.modelo.MyViewModel
import com.example.proyectodam.persistencia.DbOpenHelper
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaracteristicasLibro(dbOpenHelper: DbOpenHelper, navController: NavController, libroId: Int, myViewModel: MyViewModel = viewModel(factory = LibroViewModelFactory(dbOpenHelper))) {
    val libro = myViewModel.buscarPorId(libroId)

    libro?.let {
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
        var estanteria by remember { mutableStateOf(libro.estanteria ?: 0) }
        var estante by remember { mutableStateOf(libro.estante ?: 0) }
        var seccion by remember { mutableStateOf(libro.seccion ?: 'A')}

            Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Detalles del Libro") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        libro.portada?.let { uri ->
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        TextField(
                            value = titulo,
                            onValueChange = { titulo = it },
                            label = { Text("Título") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = autor,
                            onValueChange = { autor = it },
                            label = { Text("Autor") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = resumen,
                            onValueChange = { resumen = it },
                            label = { Text("Resumen") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 4,
                            singleLine = false
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextField(
                            value = isbn,
                            onValueChange = { isbn = it },
                            label = { Text("ISBN") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = editorial,
                            onValueChange = { editorial = it },
                            label = { Text("Editorial") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = anioPublicacion.toString(),
                            onValueChange = { anioPublicacion = it.toIntOrNull() ?: 0 },
                            label = { Text("Año de publicación") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = genero,
                            onValueChange = { genero = it },
                            label = { Text("Género") },
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = numeroPaginas.toString(),
                            onValueChange = { numeroPaginas = it.toIntOrNull() ?: 0 },
                            label = { Text("Número de páginas") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = idioma,
                            onValueChange = { idioma = it },
                            label = { Text("Idioma") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = fechaAdquisicion,
                            onValueChange = { fechaAdquisicion = it },
                            label = { Text("Fecha de adquisición") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = estanteria.toString(),
                            onValueChange = { estanteria = it.toIntOrNull() ?: 0 },
                            label = { Text("Estantería") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = estante.toString(),
                            onValueChange = { estante = it.toIntOrNull() ?: 0 },
                            label = { Text("Estante") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = seccion.toString(),
                            onValueChange = { seccion = it.firstOrNull() ?: 'A' },
                            label = { Text("Sección") }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    // Actualizar libro
                                    //myViewModel.actualizarLibro(libro.copy(titulo = titulo, autor = autor, resumen = resumen))
                                    navController.popBackStack()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Guardar")
                            }
                            Button(
                                onClick = {
                                    // Eliminar libro, si se borra se vuelve al menú, si no el botón no hace nada
                                    if (myViewModel.borrarPorId(libroId)){
                                        navController.popBackStack()
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Eliminar", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    } ?: run {
        // En caso de que no se enuentre el libro, manejaremos la excepción así
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Detalles del Libro") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
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
                Text("Libro no encontrado")
            }
        }
    }
}