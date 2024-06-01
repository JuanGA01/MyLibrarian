package com.example.proyectodam.modelo

data class Libro(
    // Identificador básico obligatorio de cada libro en nuesta app
    var id: Int? = null,
    // El libro por defecto no está prestado
    var prestado: Boolean = false,
    // La ubicación se guarda en estantería (mueble donde están los libros), estante (balda en la que están)
    // y sección (A,B,C, etc. De izquierda a derecha)
    var estanteria:Int? = null,
    var estante:Int? = null,
    var seccion:Char? = null,
    // El título es, de los datos del libro, el único obligatorio, el ISBN es muy importante pero no obligatorio
    // pues aunque no es recomendable puede haber un libro sin ISBN
    val titulo: String,
    var isbn: String? = null,
    var autor: String? = null,
    var editorial: String? = null,
    var anioPublicacion: Int? = null,
    var genero: String? = null,
    var numeroPaginas: Int? = null,
    var idioma: String? = null,
    var resumen: String? = null,
    var fechaAdquisicion: String? = null,
    // La portada es un string, pues solo guardaremos la uri de donde está la imagen, para evitar duplicidades
    // evitando así el uso innecesario de memoria
    var portada: String? = null,
    // Una variable para que el usuario agregue notas personalizadas
    var notas: String? = null,
)
