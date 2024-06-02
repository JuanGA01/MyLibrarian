package com.example.proyectodam

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectodam.modelo.Libro
import com.example.proyectodam.persistencia.DbOpenHelper
import com.example.proyectodam.persistencia.LibrosRepository
import com.example.proyectodam.ui.theme.ProyectoDAMTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.proyectodam.modelo.LibroViewModelFactory
import com.example.proyectodam.modelo.MyViewModel
import kotlinx.coroutines.CoroutineScope
import java.io.ByteArrayOutputStream

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
fun TabScreen(dbOpenHelper: DbOpenHelper, navController: NavController) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Mis Libros", "Libros Prestados")
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 15.dp, bottom = 5.dp)
    ) {
        Box(modifier = Modifier.weight(1.0f)) {
            when (tabIndex) {
                0 -> HomeScreen(dbOpenHelper, navController)
                1 -> LibrosPrestados(dbOpenHelper, navController)
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
        leadingIcon = {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_search),
                contentDescription = null
            )
        },
        placeholder = { Text("Buscar...") },
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
fun LibroCard(libro: Libro, onLongPress: (Libro) -> Unit, onTap: (Libro) -> Unit) {
    Log.d(MainActivity::class.java.name, "URI: " + libro.portada)

    val borderColor = if (libro.prestado) Color.Red else Color.Green

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, borderColor)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onLongPress(libro) },
                    onTap = { onTap(libro) }
                )
            }
    ) {
        Box {
            libro.portada?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.Transparent)
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
}

@Composable
fun HomeScreen(dbOpenHelper: DbOpenHelper, navController: NavController, myViewModel: MyViewModel = viewModel(factory = LibroViewModelFactory(dbOpenHelper))) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        myViewModel.cargarLibros()
    }

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
            ) {
                items(myViewModel.libros) { libro ->
                    LibroCard(libro = libro,
                        onLongPress = { selectedLibro -> myViewModel.cambiarEstado(selectedLibro) },
                        onTap = { selectedLibro -> navController.navigate("CaracteristicasLibro/${selectedLibro.id}") }
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp)
        ) {
            FloatingActionButton(
                onClick = { navController.navigate("AniadeLibros") }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Localized description")
            }
        }
    }
}

@Composable
fun LibrosPrestados(dbOpenHelper: DbOpenHelper, navController: NavController, myViewModel: MyViewModel = viewModel(factory = LibroViewModelFactory(dbOpenHelper))) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        myViewModel.cargarLibrosPrestados()
    }

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
            ) {
                items(myViewModel.librosPrestados) { libro ->
                    LibroCard(libro = libro,
                        onLongPress = { selectedLibro -> myViewModel.cambiarEstado(selectedLibro) },
                        onTap = { selectedLibro -> navController.navigate("CaracteristicasLibro/${selectedLibro.id}") }
                    )
                }
            }
        }
    }
}



@Composable
fun Main(modifier: Modifier = Modifier, dbOpenHelper: DbOpenHelper) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "TabScreen") {
        composable("TabScreen") {
            TabScreen(dbOpenHelper, navController)
        }
        composable("AniadeLibros") {
            AniadeLibros(dbOpenHelper, navController)
        }
        composable(
            route = "CaracteristicasLibro/{libroId}",
            arguments = listOf(navArgument("libroId") { type = NavType.IntType })
        ) { backStackEntry ->
            val libroId = backStackEntry.arguments?.getInt("libroId")
            if (libroId != null) {
                CaracteristicasLibro(dbOpenHelper, navController, libroId)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProyectoDAMTheme {
        //Main()
    }
}