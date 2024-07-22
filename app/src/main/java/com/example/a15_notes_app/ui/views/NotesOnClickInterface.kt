package com.example.a15_notes_app.ui.views

import com.example.a15_notes_app.data.model.NotesItem

interface NotesOnClickInterface {

    fun noteClickResponse(note:NotesItem)

    fun onLongPressResponse(note:NotesItem)
}