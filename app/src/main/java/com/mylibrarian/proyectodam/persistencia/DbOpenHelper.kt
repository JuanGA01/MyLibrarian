package com.mylibrarian.proyectodam.persistencia

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DB_FILE_NAME = "libros"
const val DB_VERSION = 10
class DbOpenHelper(context: Context) : SQLiteOpenHelper(context, DB_FILE_NAME, null, DB_VERSION) {

    // Al crear la base de datos, se crea la tabla libros
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE libros (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    prestado BOOLEAN NOT NULL DEFAULT 0," +
                "    estanteria INTEGER," +
                "    estante INTEGER," +
                "    seccion CHAR(1)," +
                "    titulo VARCHAR(200) NOT NULL," +
                "    isbn VARCHAR(13)," +
                "    autor VARCHAR(200)," +
                "    editorial TEXT," +
                "    anioPublicacion INTEGER," +
                "    genero TEXT," +
                "    numeroPaginas INTEGER," +
                "    idioma TEXT," +
                "    resumen TEXT," +
                "    fechaAdquisicion DATE," +
                "    portada TEXT," +
                "    notas TEXT" +
                ")")
    }

    // Al actualizar la base de datos, se elimina la tabla libros y se vuelve a crear
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE  libros")
        onCreate(db)
    }

}
