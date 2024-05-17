package com.example.proyectodam.modelo

import androidx.annotation.DrawableRes

data class Libro(
    val titulo: String,
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
    @DrawableRes val portada: Int? = null
)
