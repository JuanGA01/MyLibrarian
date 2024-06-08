package com.example.proyectodam

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.proyectodam.modelo.Libro
import com.example.proyectodam.modelo.LibroViewModelFactory
import com.example.proyectodam.modelo.MyViewModel
import com.example.proyectodam.persistencia.DbOpenHelper
import com.example.proyectodam.ui.theme.ProyectoDAMTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraDeBusqueda(query: TextFieldValue, onQueryChange: (TextFieldValue) -> Unit) {
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
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun LibroItem(libro: Libro, onTap: (Libro) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTap(libro) }
            .padding(vertical = 8.dp)
    ) {
        Text(text = libro.titulo, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = libro.autor ?: "Desconocido")
    }
}

@Composable
fun LibroCard(libro: Libro, onLongPress: (Libro) -> Unit, onTap: (Libro) -> Unit) {
    //Mostraremos el libro en blanco y negro si no esta prestado
    val colorFilter = if (!libro.prestado) {
        ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
    } else {
        null
    }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.6f)
            .clip(RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onLongPress(libro) },
                    onTap = { onTap(libro) }
                )
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
    ) {
        if (!libro.portada.isNullOrBlank()) {
            Log.d(MainActivity::class.java.name, "URI: " + libro.portada)
            AsyncImage(
                model = libro.portada,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                colorFilter = colorFilter,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun LibroCardPrestados(libro: Libro, onLongPress: (Libro) -> Unit, onTap: (Libro) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.6f)
            .clip(RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onLongPress(libro) },
                    onTap = { onTap(libro) }
                )
            }
    ) {
        if (!libro.portada.isNullOrBlank()) {
            Log.d(MainActivity::class.java.name, "URI: " + libro.portada)
            AsyncImage(
                model = libro.portada,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun HomeScreen(
    dbOpenHelper: DbOpenHelper,
    navController: NavController,
    myViewModel: MyViewModel = viewModel(factory = LibroViewModelFactory(dbOpenHelper))
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val librosFiltrados by remember { derivedStateOf { myViewModel.librosFiltrados } }
    LaunchedEffect(Unit) {
        myViewModel.cargarLibros()
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BarraDeBusqueda(
                query = searchQuery,
                onQueryChange = { newQuery ->
                    searchQuery = newQuery
                    myViewModel.cargarLibrosFiltrados(newQuery.text)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (librosFiltrados.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(8.dp)
                        .border(1.dp, Color.Gray)
                ) {
                    Column {
                        librosFiltrados.forEach { libro ->
                            LibroItem(libro = libro, onTap = {
                                navController.navigate("CaracteristicasLibro/${libro.id}")
                            })
                        }
                    }
                }
            }
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
fun LibrosPrestados(
    dbOpenHelper: DbOpenHelper,
    navController: NavController,
    myViewModel: MyViewModel = viewModel(factory = LibroViewModelFactory(dbOpenHelper))
) {
    val librosPrestados by remember { derivedStateOf { myViewModel.librosPrestados } }
    LaunchedEffect(Unit) {
        myViewModel.cargarLibrosPrestados()
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (librosPrestados.isNotEmpty()) {
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(myViewModel.librosPrestados) { libro ->
                    LibroCardPrestados(libro = libro,
                        onLongPress = { selectedLibro -> myViewModel.cambiarEstado(selectedLibro) },
                        onTap = { selectedLibro -> navController.navigate("AniadeNotas/${selectedLibro.id}") }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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
        composable(
            route = "AniadeNotas/{libroId}",
            arguments = listOf(navArgument("libroId") { type = NavType.IntType })
        ) { backStackEntry ->
            val libroId = backStackEntry.arguments?.getInt("libroId")
            if (libroId != null) {
                AniadeNotas(dbOpenHelper, navController,libroId)
            }
        }
    }
}
