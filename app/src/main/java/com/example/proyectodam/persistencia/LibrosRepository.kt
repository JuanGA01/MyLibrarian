package com.example.proyectodam.persistencia

import android.content.ContentValues
import android.database.Cursor
import com.example.proyectodam.modelo.Libro
class LibrosRepository(
    private val dbOpenHelper: DbOpenHelper
) {
    fun findAll(): List<Libro> {
        val db = dbOpenHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM libros", null)
        val libros = mutableListOf<Libro>()

        try {
            val columnIndices =
                cursor.columnNames.mapIndexed { index, name -> name to index }.toMap()

            while (cursor.moveToNext()) {
                val id = cursor.getIntOrNull(columnIndices["id"] ?: -1)
                val prestado = cursor.getStringOrNull(columnIndices["prestado"] ?: -1)?.toBoolean() ?: false
                val titulo = cursor.getString(columnIndices["titulo"] ?: -1)
                val autor = cursor.getStringOrNull(columnIndices["autor"] ?: -1)
                val isbn = cursor.getStringOrNull(columnIndices["isbn"] ?: -1)
                val editorial = cursor.getStringOrNull(columnIndices["editorial"] ?: -1)
                val anoPublicacion = cursor.getIntOrNull(columnIndices["anoPublicacion"] ?: -1)
                val genero = cursor.getStringOrNull(columnIndices["genero"] ?: -1)
                val numeroPaginas = cursor.getIntOrNull(columnIndices["numeroPaginas"] ?: -1)
                val idioma = cursor.getStringOrNull(columnIndices["idioma"] ?: -1)
                val resumen = cursor.getStringOrNull(columnIndices["resumen"] ?: -1)
                val valoracion = cursor.getIntOrNull(columnIndices["valoracion"] ?: -1)
                val estado = cursor.getStringOrNull(columnIndices["estado"] ?: -1)
                val fechaAdquisicion =
                    cursor.getStringOrNull(columnIndices["fechaAdquisicion"] ?: -1)

                val libro = Libro(
                    id = id,
                    titulo = titulo,
                    prestado = prestado,
                    autor = autor,
                    isbn = isbn,
                    editorial = editorial,
                    anoPublicacion = anoPublicacion,
                    genero = genero,
                    numeroPaginas = numeroPaginas,
                    idioma = idioma,
                    resumen = resumen,
                    valoracion = valoracion,
                    estado = estado,
                    fechaAdquisicion = fechaAdquisicion
                )
                libros.add(libro)
            }
        } finally {
            cursor.close()
            db.close()
        }

        return libros
    }

    fun findPrestados(): List<Libro> {
        val db = dbOpenHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM libros WHERE prestado = 1", null)
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
                val anoPublicacion = cursor.getIntOrNull(columnIndices["anoPublicacion"] ?: -1)
                val genero = cursor.getStringOrNull(columnIndices["genero"] ?: -1)
                val numeroPaginas = cursor.getIntOrNull(columnIndices["numeroPaginas"] ?: -1)
                val idioma = cursor.getStringOrNull(columnIndices["idioma"] ?: -1)
                val resumen = cursor.getStringOrNull(columnIndices["resumen"] ?: -1)
                val valoracion = cursor.getIntOrNull(columnIndices["valoracion"] ?: -1)
                val estado = cursor.getStringOrNull(columnIndices["estado"] ?: -1)
                val fechaAdquisicion = cursor.getStringOrNull(columnIndices["fechaAdquisicion"] ?: -1)

                val libro = Libro(
                    id = id,
                    titulo = titulo,
                    prestado = prestado,
                    autor = autor,
                    isbn = isbn,
                    editorial = editorial,
                    anoPublicacion = anoPublicacion,
                    genero = genero,
                    numeroPaginas = numeroPaginas,
                    idioma = idioma,
                    resumen = resumen,
                    valoracion = valoracion,
                    estado = estado,
                    fechaAdquisicion = fechaAdquisicion
                )
                libros.add(libro)
            }
        } finally {
            cursor.close()
            db.close()
        }

        return libros
    }

    fun findById(id: Int): Libro? {
        val db = dbOpenHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM libros WHERE id = ?", arrayOf(id.toString()))
        var libro: Libro? = null

        try {
            if (cursor.moveToFirst()) {
                val prestado = cursor.getStringOrNull(1)?.toBoolean() ?: false
                val titulo = cursor.getStringOrNull(2) ?: ""
                val autor = cursor.getStringOrNull(3)
                val isbn = cursor.getStringOrNull(4)
                val editorial = cursor.getStringOrNull(5)
                val anoPublicacion = cursor.getIntOrNull(6)
                val genero = cursor.getStringOrNull(7)
                val numeroPaginas = cursor.getIntOrNull(8)
                val idioma = cursor.getStringOrNull(9)
                val resumen = cursor.getStringOrNull(10)
                val valoracion = cursor.getIntOrNull(11)
                val estado = cursor.getStringOrNull(12)
                val fechaAdquisicion = cursor.getStringOrNull(13)

                libro = Libro(
                    id = id,
                    titulo = titulo,
                    prestado = prestado,
                    autor = autor,
                    isbn = isbn,
                    editorial = editorial,
                    anoPublicacion = anoPublicacion,
                    genero = genero,
                    numeroPaginas = numeroPaginas,
                    idioma = idioma,
                    resumen = resumen,
                    valoracion = valoracion,
                    estado = estado,
                    fechaAdquisicion = fechaAdquisicion
                )
            }
        } finally {
            cursor.close()
            db.close()
        }

        return libro
    }

    fun deleteById(id: Int): Boolean {
        // Abre la base de datos en modo escritura
        val db = dbOpenHelper.writableDatabase
        var affectedRows = 0

        try {
            // Elimina el libro con el ID especificado de la tabla "libros"
            affectedRows = db.delete("libros", "id = ?", arrayOf(id.toString()))
        } finally {
            // Cierra la base de datos
            db.close()
        }

        // Retorna true si se eliminó al menos una fila, false si no se eliminó ninguna
        return affectedRows > 0
    }

    /**
     * Inserta un nuevo libro en la base de datos.
     *
     * @param libro El libro a insertar.
     * @return El ID del nuevo libro insertado, o -1 si ocurrió un error.
     */
    fun insertLibro(libro: Libro): Long {
        // Abre la base de datos en modo escritura
        val db = dbOpenHelper.writableDatabase
        val values = ContentValues().apply {
            // Agrega los valores del libro a un objeto ContentValues para la inserción
            put("titulo", libro.titulo)
            put("prestado", if (libro.prestado) 1 else 0)
            put("autor", libro.autor)
            put("isbn", libro.isbn)
            put("editorial", libro.editorial)
            put("anoPublicacion", libro.anoPublicacion)
            put("genero", libro.genero)
            put("numeroPaginas", libro.numeroPaginas)
            put("idioma", libro.idioma)
            put("resumen", libro.resumen)
            put("valoracion", libro.valoracion)
            put("estado", libro.estado)
            put("fechaAdquisicion", libro.fechaAdquisicion)
            put("portada", libro.portada)
        }
        var newRowId: Long
        try {
            // Inserta el nuevo libro en la tabla "libros" y obtiene el ID de la nueva fila
            newRowId = db.insert("libros", null, values)
        } finally {
            // Cierra la base de datos
            db.close()
        }
        // Retorna el ID del nuevo libro insertado, o -1 si ocurrió un error
        return newRowId
    }

    fun findByTitleSubstring(substring: String): List<Libro> {
        // Abre la base de datos en modo lectura
        val db = dbOpenHelper.readableDatabase
        // Ejecuta una consulta SQL para seleccionar libros cuyos títulos contienen la subcadena
        val cursor = db.rawQuery("SELECT * FROM libros WHERE titulo LIKE ?", arrayOf("%$substring%"))
        // Lista para almacenar los libros encontrados
        val libros = mutableListOf<Libro>()

        try {
            // Mapea los nombres de las columnas a sus índices para un acceso más fácil
            val columnIndices = cursor.columnNames.mapIndexed { index, name -> name to index }.toMap()

            // Itera sobre los resultados del cursor y crea instancias de Libro
            while (cursor.moveToNext()) {
                // Obtiene los valores de las columnas para crear un nuevo libro
                val id = cursor.getIntOrNull(columnIndices["id"] ?: -1)
                val prestado = cursor.getIntOrNull(columnIndices["prestado"] ?: -1)?.let { it == 1 } ?: false
                val titulo = cursor.getStringOrNull(columnIndices["titulo"] ?: -1) ?: ""
                val autor = cursor.getStringOrNull(columnIndices["autor"] ?: -1)
                val isbn = cursor.getStringOrNull(columnIndices["isbn"] ?: -1)
                val editorial = cursor.getStringOrNull(columnIndices["editorial"] ?: -1)
                val anoPublicacion = cursor.getIntOrNull(columnIndices["anoPublicacion"] ?: -1)
                val genero = cursor.getStringOrNull(columnIndices["genero"] ?: -1)
                val numeroPaginas = cursor.getIntOrNull(columnIndices["numeroPaginas"] ?: -1)
                val idioma = cursor.getStringOrNull(columnIndices["idioma"] ?: -1)
                val resumen = cursor.getStringOrNull(columnIndices["resumen"] ?: -1)
                val valoracion = cursor.getIntOrNull(columnIndices["valoracion"] ?: -1)
                val estado = cursor.getStringOrNull(columnIndices["estado"] ?: -1)
                val fechaAdquisicion = cursor.getStringOrNull(columnIndices["fechaAdquisicion"] ?: -1)

                // Crea una instancia de Libro con los valores obtenidos y la añade a la lista
                val libro = Libro(
                    id = id,
                    titulo = titulo,
                    prestado = prestado,
                    autor = autor,
                    isbn = isbn,
                    editorial = editorial,
                    anoPublicacion = anoPublicacion,
                    genero = genero,
                    numeroPaginas = numeroPaginas,
                    idioma = idioma,
                    resumen = resumen,
                    valoracion = valoracion,
                    estado = estado,
                    fechaAdquisicion = fechaAdquisicion
                )
                libros.add(libro)
            }
        } finally {
            // Cierra el cursor y la base de datos
            cursor.close()
            db.close()
        }

        // Devuelve la lista de libros encontrados
        return libros
    }


    //Método para cambiar un libro el valor prestado haciendo que pase a estado prestado o no prestado
    fun togglePrestado(id: Int): Boolean {
        val db = dbOpenHelper.writableDatabase
        var wasUpdated = false

        try {
            // Primero, obtenemos el estado actual de "prestado"
            val cursor = db.rawQuery("SELECT prestado FROM libros WHERE id = ?", arrayOf(id.toString()))
            if (cursor.moveToFirst()) {
                val currentPrestado = cursor.getInt(0) == 1

                // Preparamos el nuevo valor para "prestado"
                val newPrestado = !currentPrestado

                // Creamos un ContentValues con el nuevo valor
                val values = ContentValues().apply {
                    put("prestado", if (newPrestado) 1 else 0)
                }

                // Actualizamos el registro en la base de datos
                val affectedRows = db.update("libros", values, "id = ?", arrayOf(id.toString()))
                wasUpdated = affectedRows > 0
            }
            cursor.close()
        } finally {
            db.close()
        }

        return wasUpdated
    }

    private fun Cursor.getIntOrNull(columnIndex: Int): Int? =
        if (columnIndex >= 0 && !isNull(columnIndex)) getInt(columnIndex) else null

    private fun Cursor.getStringOrNull(columnIndex: Int): String? =
        if (columnIndex >= 0 && !isNull(columnIndex)) getString(columnIndex) else null

}