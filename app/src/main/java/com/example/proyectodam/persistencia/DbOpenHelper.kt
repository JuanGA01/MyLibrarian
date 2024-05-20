package com.example.proyectodam.persistencia

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DB_FILE_NAME = "libros"
const val DB_VERSION = 1
class DbOpenHelper(context: Context)
    : SQLiteOpenHelper(context, DB_FILE_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE libros (" +
                "    id INTEGER," +
                "    titulo VARCHAR(200) NOT NULL," +
                "    autor VARCHAR(200)," +
                "    isbn VARCHAR(13)," +
                "    editorial TEXT," +
                "    anoPublicacion INTEGER," +
                "    genero TEXT," +
                "    numeroPaginas INTEGER," +
                "    idioma TEXT," +
                "    resumen TEXT," +
                "    valoracion INTEGER," +
                "    estado TEXT," +
                "    fechaAdquisicion DATE," +
                "    portada INTEGER" +
                ")")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE  libros")
        onCreate(db);
    }

}