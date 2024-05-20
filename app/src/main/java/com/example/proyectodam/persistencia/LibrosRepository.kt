package com.example.proyectodam.persistencia

import com.example.proyectodam.modelo.Libro

class LibrosRepository(
    private val dbOpenHelper: DbOpenHelper
) {
    fun findAll(): List<Libro> {
        val db = dbOpenHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM libros", null)
        val libros = mutableListOf<Libro>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val titulo = cursor.getString(1)
            val autor = cursor.getString(2)
            val isbn = cursor.getString(3)
            val editorial = cursor.getString(4)
            val anoPublicacion = cursor.getInt(5)
            val genero = cursor.getString(6)
            val numeroPaginas = cursor.getInt(7)
            val idioma = cursor.getString(8)
            val resumen = cursor.getString(9)
            val valoracion = cursor.getInt(10)
            val estado = cursor.getString(11)
            val fechaAdquisicion = cursor.getString(12)
            val portada = cursor.getInt(13)
            val libro = Libro(
                titulo = titulo,
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
                fechaAdquisicion = fechaAdquisicion,
                portada = portada)
            libros.add(libro)
        }
        cursor.close()
        db.close()
        return libros
    }
}