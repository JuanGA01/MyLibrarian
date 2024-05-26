package com.example.proyectodam

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ScrollView
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyectodam.modelo.Libro
import com.example.proyectodam.persistencia.DbOpenHelper
import com.example.proyectodam.persistencia.LibrosRepository
import com.example.proyectodam.ui.theme.ProyectoDAMTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoDAMTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Main(
                        modifier = Modifier.padding(innerPadding),
                        dbOpenHelper = DbOpenHelper(applicationContext)
                    )
                }
            }
        }
    }
}

@Composable
fun TabScreen(dbOpenHelper: DbOpenHelper) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Mis Libros", "Libros Prestados")
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1.0f)) {
            when (tabIndex) {
                0 -> HomeScreen(dbOpenHelper)
                1 -> LibrosPrestados(dbOpenHelper)
            }
        }
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    icon = {
                        when (index) {
                            0 -> Icon(imageVector = Icons.Default.Home, contentDescription = null)
                            1 -> Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BarraDeBusqueda (query: TextFieldValue, onQueryChange: (TextFieldValue) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Buscar...") },
        leadingIcon = {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_search),
                contentDescription = null
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun LibroCard(libro: Libro) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.4f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = libro.titulo)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Autor: ${libro.autor ?: "Desconocido"}")
            libro.resumen?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Resumen: $it")
            }
        }
    }
}

@Composable
fun HomeScreen(dbOpenHelper: DbOpenHelper) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val librosRepository = LibrosRepository(dbOpenHelper)
            val libros = librosRepository.findAll(dbOpenHelper)
            BarraDeBusqueda(
                query = searchQuery,
                onQueryChange = { newQuery -> searchQuery = newQuery }
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                items(libros.size) { index ->
                    val libro = libros[index]
                    LibroCard(libro = libro)
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp)
        ) {
            FloatingActionButton(
                onClick = { /* Acción a realizar */ }
            ){
                Icon(Icons.Filled.Add, contentDescription = "Localized description")
            }
        }
    }
}

@Composable
fun AniadeLibros() {
    val scope = rememberCoroutineScope()
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
    var resultados by remember { mutableStateOf<List<String>>(emptyList()) }
    var estanteria by remember { mutableStateOf(1) }
    var estante by remember { mutableStateOf(1) }
    var seccion by remember { mutableStateOf("") }
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
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            OutlinedTextField(
                value = estante.toString(),
                onValueChange = {
                    val value = it.toIntOrNull() ?: 0
                    estante = value.coerceIn(1, 10)},
                label = { Text("Estante") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
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
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            scope.launch {
                withContext(Dispatchers.Main) {

                }
            }
        }) {
            Text("Añadir")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column {
            resultados.forEach {
                Text(it)
            }
        }
    }
}

@Composable
fun LibrosPrestados(dbOpenHelper: DbOpenHelper) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val librosRepository = LibrosRepository(dbOpenHelper)
            val librosPrestados = librosRepository.findPrestados(dbOpenHelper)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(librosPrestados.size) { index ->
                    val libro = librosPrestados[index]
                    LibroCard(libro = libro)
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp)
        ) {
            FloatingActionButton(
                onClick = { /* Acción a realizar */ }
            ){
                Icon(Icons.Filled.Add, contentDescription = "Localized description")
            }
        }
    }
}

@Composable
fun Main(modifier: Modifier = Modifier, dbOpenHelper: DbOpenHelper) {
    Column (
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier.fillMaxSize()
    ){
        TabScreen(dbOpenHelper)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProyectoDAMTheme {
        AniadeLibros()
    }
}