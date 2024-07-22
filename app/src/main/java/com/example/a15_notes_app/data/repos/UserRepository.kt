package com.example.a15_notes_app.data.repos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.a15_notes_app.data.api.UserApi
import com.example.a15_notes_app.data.model.NetworkResult
import com.example.a15_notes_app.data.model.UserAndToken
import com.example.a15_notes_app.data.model.UserLoginData
import com.example.a15_notes_app.utils.Event
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


class UserRepository @Inject constructor(private val userApi: UserApi){

    private val _userApiResponseLiveData = MutableLiveData<Event<NetworkResult<UserAndToken>>>()
    val userApiResponseLiveData: LiveData<Event<NetworkResult<UserAndToken>>> get() = _userApiResponseLiveData

    suspend fun signup(user: UserLoginData)
    {
        _userApiResponseLiveData.postValue(Event(NetworkResult.Loading()))
        val response = userApi.signup(user)
        UpdateMutableLiveDataAsPerResponse(response)
    }

    suspend fun signin(user: UserLoginData)
    {
        _userApiResponseLiveData.postValue(Event(NetworkResult.Loading()))
        val response= userApi.signin(user)
        Log.d("@@@", "${user}")
        UpdateMutableLiveDataAsPerResponse(response)
    }


    fun UpdateMutableLiveDataAsPerResponse(response: Response<UserAndToken>)
    {
        if(response.isSuccessful && response.body()!= null)
        {
            _userApiResponseLiveData.postValue(Event(NetworkResult.Success(response.body()!!)))
        }

        else if(response.errorBody() != null)   // Case of "Error" where "api" is giving error body
        {                                       // for us to know the reason of error
            var errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            var errorMsg= errorObj.getString("message")     // Remember in our API
                        // We created JSON objects in case of errors, and they all had names "message"

            _userApiResponseLiveData.postValue(Event(NetworkResult.Error(null, errorMsg)))
        }

        else{           // When we don't know what the error is
            _userApiResponseLiveData
                .postValue(Event(NetworkResult.Error(null, "Something went wrong, but we dont know what")))
        }
    }
}
