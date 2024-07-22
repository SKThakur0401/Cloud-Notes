package com.example.a15_notes_app.ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a15_notes_app.data.model.NetworkResult
import com.example.a15_notes_app.data.model.Note
import com.example.a15_notes_app.data.model.NotesItem
import com.example.a15_notes_app.data.repos.NotesRepository
import com.example.a15_notes_app.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val notesRepository: NotesRepository): ViewModel() {

    var notesLiveData: LiveData<Event<NetworkResult<ArrayList<NotesItem>>>> = notesRepository.notesLiveData
    var statusLiveData: LiveData<Event<NetworkResult<Pair<Boolean, String>>>> = notesRepository.statusLiveData
    fun getNotes()
    {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.getMyNotes()
        }
    }

    fun deleteNote(noteId:String)
    {
        viewModelScope.launch(Dispatchers.IO){
            notesRepository.deleteMyNote(noteId)
        }
    }

    fun createNote(note:Note)
    {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.createMyNote(note)
        }
    }

    fun updateNote(noteId: String, note: Note)
    {
        viewModelScope.launch(Dispatchers.IO){
            notesRepository.updateMyNote(noteId, note)
        }
    }
}