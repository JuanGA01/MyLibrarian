package com.example.proyectodam.modelo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectodam.persistencia.DbOpenHelper
import com.example.proyectodam.persistencia.LibrosRepository
import androidx.lifecycle.ViewModelProvider

class LibroViewModelFactory(private val dbHelper: DbOpenHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyViewModel(dbHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MyViewModel(private val dbHelper: DbOpenHelper): ViewModel() {
    var libros by  mutableStateOf(listOf<Libro>())
        private set
    var librosPrestados by  mutableStateOf(listOf<Libro>())
        private set
    var librosNoPrestados by  mutableStateOf(listOf<Libro>())
        private set
    var librosFiltrados by  mutableStateOf(listOf<Libro>())
        private set

    init {
        cargarLibros()
        cargarLibrosPrestados()
        cargarLibrosNoPrestados()
    }

    fun addLibro(dbOpenHelper: DbOpenHelper, libro:Libro) {
        val librosRepository = LibrosRepository(dbOpenHelper)
        librosRepository.insertLibro(dbOpenHelper, libro)
        cargarLibros()
        cargarLibrosPrestados()
        cargarLibrosNoPrestados()
    }

    fun cargarLibros() {
        libros = LibrosRepository(dbHelper).findAll()
    }

    fun cargarLibrosPrestados(){
        librosPrestados = LibrosRepository(dbHelper).findAllPrestados()
    }

    fun cargarLibrosNoPrestados(){
        librosNoPrestados = LibrosRepository(dbHelper).findAllNoPrestados()
    }

    fun cargarLibrosFiltrados(newQuery:String) {
        libros = LibrosRepository(dbHelper).findByTitleSubstring(newQuery)
    }

    fun cambiarEstado(libro: Libro) {
        val librosRepository = LibrosRepository(dbHelper)
        libro.id?.let { librosRepository.togglePrestado(it) }
        cargarLibros()
        cargarLibrosPrestados()
        cargarLibrosPrestados()
    }

    fun buscarPorId(id: Int): Libro? {
        return LibrosRepository(dbHelper).findById(id)
    }

    fun borrarPorId(id: Int): Boolean {
        return LibrosRepository(dbHelper).deleteById(id)
    }

    fun actualizarLibro(libro: Libro): Boolean {
        return LibrosRepository(dbHelper).updateLibro(libro)
    }

}