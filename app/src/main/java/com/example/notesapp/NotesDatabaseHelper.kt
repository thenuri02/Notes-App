package com.example.notesapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NotesDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

//    null is the curser factory and that creates a pointer to point the data in the db

    companion object{
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allnotes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
    }

    //create tables
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT)"
        db?.execSQL(createTableQuery)
        //run using execQL method
    }

    //drop table
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db) //new table created using onCreated method
    }

    fun insertNote(note: Note){
        val db = writableDatabase     //can be modified
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllNotes(): List<Note>{
        val noteList = mutableListOf<Note>()  //can be modified or fleixible mutable
        val db = readableDatabase   //read data
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))  //retrieve data in current raw,cursor
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

            val note= Note(id, title, content)
            noteList.add(note)
        }

        cursor.close()
        db.close()
        return noteList

    }

    //updating
    fun updateNote(note: Note){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)

        }
        //which row to be updated
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    //check this
    fun getNoteById(noteId: Int): Note? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(noteId.toString()))
        var note: Note? = null
        cursor.use {
            if (it.moveToFirst()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
                val title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE))
                val content = it.getString(it.getColumnIndexOrThrow(COLUMN_CONTENT))
                note = Note(id, title, content)
            }
        }
        db.close()
        return note
    }



    fun deleteNote(noteId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }


}