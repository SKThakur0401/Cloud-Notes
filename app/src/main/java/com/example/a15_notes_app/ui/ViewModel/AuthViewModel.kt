package com.example.a15_notes_app.ui.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a15_notes_app.data.api.NotesApi
import com.example.a15_notes_app.data.model.NetworkResult
import com.example.a15_notes_app.data.model.NotesItem
import com.example.a15_notes_app.data.model.UserAndToken
import com.example.a15_notes_app.data.model.UserLoginData
import com.example.a15_notes_app.data.repos.UserRepository
import com.example.a15_notes_app.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel(){

    val userApiResponseLD: LiveData<Event<NetworkResult<UserAndToken>>> get() = userRepository.userApiResponseLiveData

    fun signIn(user: UserLoginData)
    {
        viewModelScope.launch(Dispatchers.IO){
            userRepository.signin(user)
        }
    }

    fun signUp(user: UserLoginData)
    {
        viewModelScope.launch(Dispatchers.IO){
            userRepository.signup(user)
        }
    }



    fun basicValidations(user: UserLoginData, isLogin:Boolean):Pair<Boolean, String>
    {
        if(user.email.isEmpty() || user.password.isEmpty()) return Pair(false, "Please fill all fields")

        if(!isLogin && user.username.isEmpty()) return Pair(false, "Please fill all fields")

        return Pair(true, "")
    }

}