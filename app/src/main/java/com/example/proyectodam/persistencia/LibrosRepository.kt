package com.example.proyectodam.persistencia

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import com.example.proyectodam.modelo.Libro

class LibrosRepository( private val dbOpenHelper: DbOpenHelper) {

    //Función para sacar todos los libros
    fun findAll(): List<Libro> {
        val db = dbOpenHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM libros", null)
        val libros = mutableListOf<Libro>()
        try {
            if (cursor.moveToFirst()) {
                val columnIndices = cursor.columnNames.mapIndexed { index, name -> name to index }.toMap()
                do {
                    val id = cursor.getIntOrNull(columnIndices["id"] ?: -1)
                    val prestado = cursor.getIntOrNull(columnIndices["prestado"] ?: -1)?.let { it == 1 }
                    val titulo = cursor.getStringOrNull(columnIndices["titulo"] ?: -1) ?: ""
                    val autor = cursor.getStringOrNull(columnIndices["autor"] ?: -1)
                    val isbn = cursor.getStringOrNull(columnIndices["isbn"] ?: -1)
                    val editorial = cursor.getStringOrNull(columnIndices["editorial"] ?: -1)
                    val anioPublicacion = cursor.getIntOrNull(columnIndices["anioPublicacion"] ?: -1)
                    val genero = cursor.getStringOrNull(columnIndices["genero"] ?: -1)
                    val numeroPaginas = cursor.getIntOrNull(columnIndices["numeroPaginas"] ?: -1)
                    val idioma = cursor.getStringOrNull(columnIndices["idioma"] ?: -1)
                    val resumen = cursor.getStringOrNull(columnIndices["resumen"] ?: -1)
                    val fechaAdquisicion = cursor.getStringOrNull(columnIndices["fechaAdquisicion"] ?: -1)
                    val portada = cursor.getStringOrNull(columnIndices["portada"] ?: -1)
                    val notas = cursor.getStringOrNull(columnIndices["notas"] ?: -1)
                    val estanteria = cursor.getIntOrNull(columnIndices["estanteria"] ?: -1)
                    val estante = cursor.getIntOrNull(columnIndices["estante"] ?: -1)
                    val seccion = cursor.getStringOrNull(columnIndices["seccion"] ?: -1)?.firstOrNull()
                    val libro = Libro(
                        id = id,
                        prestado = prestado ?: false,
                        estanteria = estanteria,
                        estante = estante,
                        seccion = seccion,
                        titulo = titulo,
                        isbn = isbn,
                        autor = autor,
                        editorial = editorial,
                        anioPublicacion = anioPublicacion,
                        genero = genero,
                        numeroPaginas = numeroPaginas,
                        idioma = idioma,
                        resumen = resumen,
                        fechaAdquisicion = fechaAdquisicion,
                        portada = portada,
                        notas = notas
                    )
                    libros.add(libro)
                } while (cursor.moveToNext())
            }
        } finally {
            cursor.close()
            db.close()
        }
        return libros
    }


    // Función para sacar los libros prestados
    fun findAllPrestados(): List<Libro> {
        val db = dbOpenHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM libros WHERE prestado = 1", null)
        val libros = mutableListOf<Libro>()
        try {
            val columnIndices = cursor.columnNames.mapIndexed { index, name -> name to index }.toMap()
            while (cursor.moveToNext()) {
                val id = cursor.getIntOrNull(columnIndices["id"] ?: -1)
                val prestado = cursor.getIntOrNull(columnIndices["prestado"] ?: -1)?.let { it == 1 } ?: false
                val titulo = cursor.getString(columnIndices["titulo"] ?: -1)
                val autor = cursor.getStringOrNull(columnIndices["autor"] ?: -1)
                val isbn = cursor.getStringOrNull(columnIndices["isbn"] ?: -1)
                val editorial = cursor.getStringOrNull(columnIndices["editorial"] ?: -1)
                val anioPublicacion = cursor.getIntOrNull(columnIndices["anioPublicacion"] ?: -1)
                val genero = cursor.getStringOrNull(columnIndices["genero"] ?: -1)
                val numeroPaginas = cursor.getIntOrNull(columnIndices["numeroPaginas"] ?: -1)
                val idioma = cursor.getStringOrNull(columnIndices["idioma"] ?: -1)
                val resumen = cursor.getStringOrNull(columnIndices["resumen"] ?: -1)
                val fechaAdquisicion = cursor.getStringOrNull(columnIndices["fechaAdquisicion"] ?: -1)
                val portada = cursor.getStringOrNull(columnIndices["portada"] ?: -1)
                val notas = cursor.getStringOrNull(columnIndices["notas"] ?: -1)
                val estanteria = cursor.getIntOrNull(columnIndices["estanteria"] ?: -1)
                val estante = cursor.getIntOrNull(columnIndices["estante"] ?: -1)
                val seccion = cursor.getStringOrNull(columnIndices["seccion"] ?: -1)?.firstOrNull()
                val libro = Libro(
                    id = id,
                    prestado = prestado,
                    estanteria = estanteria,
                    estante = estante,
                    seccion = seccion,
                    titulo = titulo,
                    isbn = isbn,
                    autor = autor,
                    editorial = editorial,
                    anioPublicacion = anioPublicacion,
                    genero = genero,
                    numeroPaginas = numeroPaginas,
                    idioma = idioma,
                    resumen = resumen,
                    fechaAdquisicion = fechaAdquisicion,
                    portada = portada,
                    notas = notas
                )
                libros.add(libro)
            }
        } finally {
            cursor.close()
            db.close()
        }
        return libros
    }

    // Función para sacar los libros no prestados
    fun findAllNoPrestados(): List<Libro> {
        val db = dbOpenHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM libros WHERE prestado = 0", null)
        val libros = mutableListOf<Libro>()
        try {
            val columnIndices = cursor.columnNames.mapIndexed { index, name -> name to index }.toMap()
            while (cursor.moveToNext()) {
                val id = cursor.getIntOrNull(columnIndices["id"] ?: -1)
                val prestado = cursor.getIntOrNull(columnIndices["prestado"] ?: -1)?.let { it == 1 } ?: false
                val titulo = cursor.getString(columnIndices["titulo"] ?: -1)
                val autor = cursor.getStringOrNull(columnIndices["autor"] ?: -1)
                val isbn = cursor.getStringOrNull(columnIndices["isbn"] ?: -1)
                val editorial = cursor.getStringOrNull(columnIndices["editorial"] ?: -1)
                val anioPublicacion = cursor.getIntOrNull(columnIndices["anioPublicacion"] ?: -1)
                val genero = cursor.getStringOrNull(columnIndices["genero"] ?: -1)
                val numeroPaginas = cursor.getIntOrNull(columnIndices["numeroPaginas"] ?: -1)
                val idioma = cursor.getStringOrNull(columnIndices["idioma"] ?: -1)
                val resumen = cursor.getStringOrNull(columnIndices["resumen"] ?: -1)
                val fechaAdquisicion = cursor.getStringOrNull(columnIndices["fechaAdquisicion"] ?: -1)
                val portada = cursor.getStringOrNull(columnIndices["portada"] ?: -1)
                val notas = cursor.getStringOrNull(columnIndices["notas"] ?: -1)
                val estanteria = cursor.getIntOrNull(columnIndices["estanteria"] ?: -1)
                val estante = cursor.getIntOrNull(columnIndices["estante"] ?: -1)
                val seccion = cursor.getStringOrNull(columnIndices["seccion"] ?: -1)?.firstOrNull()
                val libro = Libro(
                    id = id,
                    prestado = prestado,
                    estanteria = estanteria,
                    estante = estante,
                    seccion = seccion,
                    titulo = titulo,
                    isbn = isbn,
                    autor = autor,
                    editorial = editorial,
                    anioPublicacion = anioPublicacion,
                    genero = genero,
                    numeroPaginas = numeroPaginas,
                    idioma = idioma,
                    resumen = resumen,
                    fechaAdquisicion = fechaAdquisicion,
                    portada = portada,
                    notas = notas
                )
                libros.add(libro)
            }
        } finally {
            cursor.close()
            db.close()
        }
        return libros
    }

    // Función para cambiar a un libro el valor de prestado haciendo que pase a estado no prestado o viceversa
    fun togglePrestado(id: Int): Boolean {
        val db = dbOpenHelper.writableDatabase
        var wasUpdated = false
        try {
            val cursor = db.rawQuery("SELECT prestado FROM libros WHERE id = ?", arrayOf(id.toString()))
            if (cursor.moveToFirst()) {
                val currentPrestado = cursor.getInt(0) == 1
                val newPrestado = !currentPrestado
                val values = ContentValues().apply { put("prestado", if (newPrestado) 1 else 0) }
                val affectedRows = db.update("libros", values, "id = ?", arrayOf(id.toString()))
                wasUpdated = affectedRows > 0
            }
            cursor.close()
        } finally {
            db.close()
        }
        return wasUpdated
    }

    // Función para insertar libros, retorna el ID del nuevo libro o -1 si ocurrió un error
    fun insertLibro(dbOpenHelper: SQLiteOpenHelper, libro: Libro): Long {
        val db = dbOpenHelper.writableDatabase
        val values = ContentValues().apply {
            put("titulo", libro.titulo)
            put("prestado", if (libro.prestado) 1 else 0)
            put("autor", libro.autor)
            put("isbn", libro.isbn)
            put("editorial", libro.editorial)
            put("anioPublicacion", libro.anioPublicacion)
            put("genero", libro.genero)
            put("numeroPaginas", libro.numeroPaginas)
            put("idioma", libro.idioma)
            put("resumen", libro.resumen)
            put("fechaAdquisicion", libro.fechaAdquisicion)
            put("portada", libro.portada)
            put("notas", libro.notas)
            put("estanteria", libro.estanteria)
            put("estante", libro.estante)
            put("seccion", libro.seccion?.toString())
            put("portada", libro.portada)
        }
        var newRowId: Long
        try {
            newRowId = db.insert("libros", null, values)
        } finally {
            db.close()
        }
        return newRowId
    }

    // Función para buscar un libro por su Id
    fun findById(id: Int): Libro? {
        val db = dbOpenHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM libros WHERE id = ?", arrayOf(id.toString()))
        var libro: Libro? = null
        try {
            if (cursor.moveToFirst()) {
                val columnIndices = cursor.columnNames.mapIndexed { index, name -> name to index }.toMap()
                val prestado = cursor.getIntOrNull(columnIndices["prestado"] ?: -1)?.let { it == 1 } ?: false
                val titulo = cursor.getString(columnIndices["titulo"] ?: -1)
                val autor = cursor.getStringOrNull(columnIndices["autor"] ?: -1)
                val isbn = cursor.getStringOrNull(columnIndices["isbn"] ?: -1)
                val editorial = cursor.getStringOrNull(columnIndices["editorial"] ?: -1)
                val anioPublicacion = cursor.getIntOrNull(columnIndices["anioPublicacion"] ?: -1)
                val genero = cursor.getStringOrNull(columnIndices["genero"] ?: -1)
                val numeroPaginas = cursor.getIntOrNull(columnIndices["numeroPaginas"] ?: -1)
                val idioma = cursor.getStringOrNull(columnIndices["idioma"] ?: -1)
                val resumen = cursor.getStringOrNull(columnIndices["resumen"] ?: -1)
                val fechaAdquisicion = cursor.getStringOrNull(columnIndices["fechaAdquisicion"] ?: -1)
                val portada = cursor.getStringOrNull(columnIndices["portada"] ?: -1)
                val notas = cursor.getStringOrNull(columnIndices["notas"] ?: -1)
                val estanteria = cursor.getIntOrNull(columnIndices["estanteria"] ?: -1)
                val estante = cursor.getIntOrNull(columnIndices["estante"] ?: -1)
                val seccion = cursor.getStringOrNull(columnIndices["seccion"] ?: -1)?.firstOrNull()
                libro = Libro(
                    id = id,
                    prestado = prestado,
                    estanteria = estanteria,
                    estante = estante,
                    seccion = seccion,
                    titulo = titulo,
                    isbn = isbn,
                    autor = autor,
                    editorial = editorial,
                    anioPublicacion = anioPublicacion,
                    genero = genero,
                    numeroPaginas = numeroPaginas,
                    idioma = idioma,
                    resumen = resumen,
                    fechaAdquisicion = fechaAdquisicion,
                    portada = portada,
                    notas = notas
                )
            }
        } finally {
            cursor.close()
            db.close()
        }
        return libro
    }

    // Función para borrar un libro por su Id, si borra el libro retorna true
    fun deleteById(id: Int): Boolean {
        val db = dbOpenHelper.writableDatabase
        var affectedRows = 0
        try {
            affectedRows = db.delete("libros", "id = ?", arrayOf(id.toString()))
        } finally {
            db.close()
        }
        return affectedRows > 0
    }

    //Función para actualizar un libro
    fun updateLibro(libro: Libro): Boolean {
        val db = dbOpenHelper.writableDatabase
        val values = ContentValues().apply {
            put("titulo", libro.titulo)
            put("prestado", if (libro.prestado) 1 else 0)
            put("autor", libro.autor)
            put("isbn", libro.isbn)
            put("editorial", libro.editorial)
            put("anioPublicacion", libro.anioPublicacion)
            put("genero", libro.genero)
            put("numeroPaginas", libro.numeroPaginas)
            put("idioma", libro.idioma)
            put("resumen", libro.resumen)
            put("fechaAdquisicion", libro.fechaAdquisicion)
            put("portada", libro.portada)
            put("notas", libro.notas)
            put("estanteria", libro.estanteria)
            put("estante", libro.estante)
            put("seccion", libro.seccion?.toString())
        }
        var affectedRows = 0
        try {
            affectedRows = db.update("libros", values, "id = ?", arrayOf(libro.id.toString()))
        } finally {
            db.close()
        }
        return affectedRows > 0
    }









    // Función que devuelve la lista de libros encontrados por su título
    fun findByTitleSubstring(dbOpenHelper: SQLiteOpenHelper, substring: String): List<Libro> {
        val db = dbOpenHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM libros WHERE titulo LIKE ?", arrayOf("%$substring%"))
        val libros = mutableListOf<Libro>()
        try {
            val columnIndices = cursor.columnNames.mapIndexed { index, name -> name to index }.toMap()
            while (cursor.moveToNext()) {
                val id = cursor.getIntOrNull(columnIndices["id"] ?: -1)
                val prestado = cursor.getIntOrNull(columnIndices["prestado"] ?: -1)?.let { it == 1 } ?: false
                val titulo = cursor.getStringOrNull(columnIndices["titulo"] ?: -1) ?: ""
                val autor = cursor.getStringOrNull(columnIndices["autor"] ?: -1)
                val isbn = cursor.getStringOrNull(columnIndices["isbn"] ?: -1)
                val editorial = cursor.getStringOrNull(columnIndices["editorial"] ?: -1)
                val anioPublicacion = cursor.getIntOrNull(columnIndices["anioPublicacion"] ?: -1)
                val genero = cursor.getStringOrNull(columnIndices["genero"] ?: -1)
                val numeroPaginas = cursor.getIntOrNull(columnIndices["numeroPaginas"] ?: -1)
                val idioma = cursor.getStringOrNull(columnIndices["idioma"] ?: -1)
                val resumen = cursor.getStringOrNull(columnIndices["resumen"] ?: -1)
                val fechaAdquisicion = cursor.getStringOrNull(columnIndices["fechaAdquisicion"] ?: -1)
                val portada = cursor.getStringOrNull(columnIndices["portada"] ?: -1)
                val notas = cursor.getStringOrNull(columnIndices["notas"] ?: -1)
                val estanteria = cursor.getIntOrNull(columnIndices["estanteria"] ?: -1)
                val estante = cursor.getIntOrNull(columnIndices["estante"] ?: -1)
                val seccion = cursor.getStringOrNull(columnIndices["seccion"] ?: -1)?.firstOrNull()
                val libro = Libro(
                    id = id,
                    prestado = prestado,
                    estanteria = estanteria,
                    estante = estante,
                    seccion = seccion,
                    titulo = titulo,
                    isbn = isbn,
                    autor = autor,
                    editorial = editorial,
                    anioPublicacion = anioPublicacion,
                    genero = genero,
                    numeroPaginas = numeroPaginas,
                    idioma = idioma,
                    resumen = resumen,
                    fechaAdquisicion = fechaAdquisicion,
                    portada = portada,
                    notas = notas
                )
                libros.add(libro)
            }
        } finally {
            cursor.close()
            db.close()
        }
        return libros
    }

    // Devuelve el valor entero de la columna si es válida y no es nula, de lo contrario devuelve null
    private fun Cursor.getIntOrNull(columnIndex: Int): Int? =
        if (columnIndex >= 0 && !isNull(columnIndex)) getInt(columnIndex) else null

    // Devuelve el valor de cadena de la columna si es válida y no es nula, de lo contrario devuelve null
    private fun Cursor.getStringOrNull(columnIndex: Int): String? =
        if (columnIndex >= 0 && !isNull(columnIndex)) getString(columnIndex) else null

}