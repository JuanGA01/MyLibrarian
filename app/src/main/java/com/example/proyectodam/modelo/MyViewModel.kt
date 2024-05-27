package com.example.proyectodam.modelo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectodam.persistencia.DbOpenHelper
import com.example.proyectodam.persistencia.LibrosRepository

class MyViewModel: ViewModel() {
    var libros by  mutableStateOf(listOf<Libro>())
        private set
    fun addLibro(dbOpenHelper: DbOpenHelper, libro:Libro) {
        val librosRepository = LibrosRepository(dbOpenHelper)
        librosRepository.insertLibro(
            dbOpenHelper,libro
            )
        cargarLibros(dbOpenHelper)
    }

    fun cargarLibros(dbOpenHelper: DbOpenHelper) {
        libros = LibrosRepository(dbOpenHelper).findAll()
    }
}