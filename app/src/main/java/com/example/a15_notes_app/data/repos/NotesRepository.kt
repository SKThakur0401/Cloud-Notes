package com.example.a15_notes_app.data.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.a15_notes_app.data.api.NotesApi
import com.example.a15_notes_app.data.model.NetworkResult
import com.example.a15_notes_app.data.model.Note
import com.example.a15_notes_app.data.model.NotesItem
import com.example.a15_notes_app.utils.Event
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NotesRepository @Inject constructor(private val notesApi:NotesApi){

    private val _notesLiveData = MutableLiveData<Event<NetworkResult<ArrayList<NotesItem>>>>()
    val notesLiveData: LiveData<Event<NetworkResult<ArrayList<NotesItem>>>> = _notesLiveData

    private val _statusLiveData= MutableLiveData<Event<NetworkResult<Pair<Boolean, String>>>>()
    val statusLiveData: LiveData<Event<NetworkResult<Pair<Boolean, String>>>> = _statusLiveData

    suspend fun getMyNotes()
    {
        _notesLiveData.postValue(Event(NetworkResult.Loading()))
        val response = notesApi.getNotes()

        if(response.isSuccessful && response.body() != null)
        {
            val res = Event(NetworkResult.Success(data=response.body()!!))
            _notesLiveData.postValue(res)
        }

        else if(response.errorBody() != null)
        {
            var errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            var errorMsg= errorObj.getString("message")

            val res = Event(NetworkResult.Error( msg= errorMsg,data=response.body()!!))
            _notesLiveData.postValue(res)
        }

        else{
            val res = Event(NetworkResult.Error(msg= "Something Went wrong, and we don't know what!", data = null))
//            _notesLiveData.postValue(res)
        }
    }

    suspend fun deleteMyNote(noteId:String)
    {
        _notesLiveData.postValue(Event(NetworkResult.Loading()))
        val response = notesApi.deleteNote(noteId)

        handleResponses(response, "Note Deleted!")
    }

    suspend fun updateMyNote(noteId:String, note: Note)
    {
        val response = notesApi.updateNote(noteId, note)

        handleResponses(response, "Note Updated!")
    }

    suspend fun createMyNote(note:Note)
    {
        val response= notesApi.postNote(note)
        handleResponses(response, "Note Created!")
    }

    fun handleResponses(response: Response<NotesItem>, msg:String)
    {
        if(response.isSuccessful && response.body() != null)
        {

            _statusLiveData.postValue(Event(NetworkResult.Success(Pair(true, msg))))
        }

        else _statusLiveData.postValue(Event(NetworkResult.Success(Pair(false, "Something idk went wrong"))))
    }
}

