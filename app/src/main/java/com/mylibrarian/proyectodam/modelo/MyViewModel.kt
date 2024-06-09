package com.mylibrarian.proyectodam.modelo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mylibrarian.proyectodam.persistencia.DbOpenHelper
import com.mylibrarian.proyectodam.persistencia.LibrosRepository
import androidx.lifecycle.ViewModelProvider

class LibroViewModelFactory(private val dbHelper: DbOpenHelper) : ViewModelProvider.Factory {
    // Esta función es responsable de crear instancias de ViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica si la clase ViewModel es de tipo MyViewModel
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Devuelve una nueva instancia de MyViewModel con dbHelper como parámetro
            return MyViewModel(dbHelper) as T
        }
        // Lanza una excepción si la clase ViewModel es desconocida
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MyViewModel(private val dbHelper: DbOpenHelper): ViewModel() {

    // Estado mutable para la lista de todos los libros
    var libros by mutableStateOf(listOf<Libro>())
        private set

    // Estado mutable para la lista de libros prestados
    var librosPrestados by mutableStateOf(listOf<Libro>())
        private set

    // Estado mutable para la lista de libros filtrados
    var librosFiltrados by mutableStateOf(listOf<Libro>())
        private set

    // Bloque inicializador para cargar libros y libros prestados
    init {
        cargarLibros()
        cargarLibrosPrestados()
    }

    // Función para agregar un nuevo libro a la base de datos y actualizar las listas
    fun addLibro(dbOpenHelper: DbOpenHelper, libro: Libro) {
        val librosRepository = LibrosRepository(dbOpenHelper)
        librosRepository.insertLibro(dbOpenHelper, libro)
        cargarLibros()
        cargarLibrosPrestados()
    }

    // Función para cargar todos los libros de la base de datos
    fun cargarLibros() {
        libros = LibrosRepository(dbHelper).findAll()
    }

    // Función para cargar todos los libros prestados de la base de datos
    fun cargarLibrosPrestados() {
        librosPrestados = LibrosRepository(dbHelper).findAllPrestados()
    }

    // Función para cargar libros que coinciden con una subcadena de título dada
    fun cargarLibrosFiltrados(newQuery: String) {
        libros = LibrosRepository(dbHelper).findByTitleSubstring(newQuery)
    }

    // Función para cambiar el estado de préstamo de un libro y actualizar las listas
    fun cambiarEstado(libro: Libro) {
        val librosRepository = LibrosRepository(dbHelper)
        libro.id?.let { librosRepository.togglePrestado(it) }
        cargarLibros()
        cargarLibrosPrestados()
    }

    // Función para buscar un libro por su ID
    fun buscarPorId(id: Int): Libro? {
        return LibrosRepository(dbHelper).findById(id)
    }

    // Función para eliminar un libro por su ID y devolver el resultado
    fun borrarPorId(id: Int): Boolean {
        return LibrosRepository(dbHelper).deleteById(id)
    }

    // Función para actualizar la información de un libro y devolver el resultado
    fun actualizarLibro(libro: Libro): Boolean {
        return LibrosRepository(dbHelper).updateLibro(libro)
    }

}
