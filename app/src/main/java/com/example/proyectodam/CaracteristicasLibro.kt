package com.example.proyectodam

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectodam.modelo.LibroViewModelFactory
import com.example.proyectodam.modelo.MyViewModel
import com.example.proyectodam.persistencia.DbOpenHelper

@Composable
fun CaracteristicasLibro(dbOpenHelper: DbOpenHelper, navController: NavController, myViewModel: MyViewModel = viewModel(factory = LibroViewModelFactory(dbOpenHelper))) {

}