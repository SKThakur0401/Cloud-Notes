package com.example.a15_notes_app.data.model

sealed class NetworkResult<T>(val data:T ?= null, val msg:String?= null){

    class Loading<T>:NetworkResult<T>()
    class Error<T>(data:T?, msg:String?):NetworkResult<T>(data, msg)
    class Success<T>(data:T):NetworkResult<T>(data)
}
