package com.example.proyectodam.modelo

import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes

data class Libro(
    var id: Int? = null,
    val titulo: String,
    var prestado: Boolean = false,
    var autor: String? = null,
    var isbn: String? = null,
    var editorial: String? = null,
    var anoPublicacion: Int? = null,
    var genero: String? = null,
    var numeroPaginas: Int? = null,
    var idioma: String? = null,
    var resumen: String? = null,
    var valoracion: Int? = null,
    var estado: String? = null,
    var fechaAdquisicion: String? = null,
    var portada: ByteArray? = null
)
