package com.example.noteapp.data

import com.example.noteapp.model.Note

class NotesDataSource{
    fun loadNotes():List<Note> {
        return listOf(
            Note(title = "hasan", description = "emre"),
            Note(title = "veysel", description = "ugurlu"),
            Note(title = "mehmet", description = "ali"),
            Note(title = "ferhat", description = "bozkurt"),
            Note(title = "okan", description = "mert"),
            Note(title = "veysel", description = "ugurlu"),
            Note(title = "mehmet", description = "ali"),
            Note(title = "ferhat", description = "bozkurt"),
            Note(title = "okan", description = "mert"),
        )
    }
}