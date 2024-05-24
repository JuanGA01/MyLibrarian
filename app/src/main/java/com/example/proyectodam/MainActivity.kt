package com.example.proyectodam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectodam.modelo.Libro
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
                var librosRepository<lista> LibrosRepository = new LibrosRepository();
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Main(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TabScreen() {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Mis Libros", "Libros Prestados", "Configuración")

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1.0f)) {
            when (tabIndex) {
                //fix me
                0 -> HomeScreen()
                1 -> LibrosPrestados()
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
                            2 -> Icon(imageVector = Icons.Default.Settings, contentDescription = null)
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
fun HomeScreen() {
    val libros = listOf(
        Libro(
            titulo = "Cien años de soledad",
            autor = "Gabriel García Márquez",
            resumen = "La obra maestra del realismo mágico."
        ),
        Libro(
            titulo = "El principito",
            autor = "Antoine de Saint-Exupéry",
            resumen = "Un relato filosófico sobre la amistad y la humanidad."
        ),
        Libro(
            titulo = "1984",
            autor = "George Orwell",
            resumen = "Una novela distópica sobre el control totalitario."
        )
    )
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
fun AniadeLibros(){
    var tituloLibro by remember { mutableStateOf(TextFieldValue()) }
    var resultados by remember { mutableStateOf<List<String>>(emptyList()) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = tituloLibro,
            onValueChange = { tituloLibro = it },
            label = { Text("Título del libro") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            scope.launch {
                val librosEncontrados = buscarLibros(tituloLibro.text)
                withContext(Dispatchers.Main) {
                    resultados = librosEncontrados
                }
            }
        }) {
            Text("Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar resultados de la búsqueda
        Column {
            resultados.forEach {
                Text(it)
            }
        }
    }
}

suspend fun buscarLibros(titulo: String): List<String> {
    // Aquí realizarías la llamada a la API para buscar libros con el título proporcionado
    // Por ahora, solo devolveré una lista ficticia de resultados
    return listOf("Libro 1", "Libro 2", "Libro 3")
}

@Composable
fun LibrosPrestados() {

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                ListaLibros =
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
fun Main(modifier: Modifier = Modifier) {
    Column (
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier.fillMaxSize()
    ){
        TabScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProyectoDAMTheme {
        //Main()
        //AniadeLibros()
        LibrosPrestados()
    }
}