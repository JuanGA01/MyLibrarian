package com.example.proyectodam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                0 -> HomeScreen()
                1 -> LibrosPrestados()
                2 -> SettingsScreen()
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
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to HomeScreen!",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    //color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "This is your home screen content.",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    //color = Color.Black
                )
            )
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to prestados",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                //color = Color.Black
            )
        )
    }
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to setings",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                //color = Color.Black
            )
        )
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
        Main()
        //AniadeLibros()
    }
}