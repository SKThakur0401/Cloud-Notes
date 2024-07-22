package com.example.a15_notes_app.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences(Constants.SHARED_PREFS_XML_FILE_NAME, Context.MODE_PRIVATE)

    // Here "SHARED_PREFS_XML_FILE_NAME" is name of XML file in which data will be stored
    // "Shared preferences" is basically used to store data on our mobile regarding things...
    // For example, after Logging-in once, an app doesn't ask you to login every time right?
    // So it "probably" stores your log-in credentials in shared preferences... or the token generated
    // in shared preferences..
    // In shared preferences file data is stored in "Key-value" pair

    private var isLogged_In= false

    fun saveToken(token:String)
    {
        var editor = prefs.edit()
        editor.putString(Constants.USER_TOKER, token)       // Here data is stored in "Key-value" pair, where
                                                        // "Constants.USER_TOKEN" is key and later is value

        editor.putBoolean(Constants.HAS_USER_SIGNED_IN, true)
        editor.apply()
    }

    fun getToken(userToker: String, nothing: Nothing?): String?
    {
        return prefs.getString(Constants.USER_TOKER, null)      // The first parameter is "Key"
                // 2nd is null idky, This fun will return the value corresponding to that key
    }


    fun change_user_signIn_status_to(signIn_status:Boolean)
    {
        var editor = prefs.edit()
        editor.putBoolean(Constants.HAS_USER_SIGNED_IN, signIn_status)
        editor.apply()
    }

    fun isLoggedIn():Boolean?
    {
        return prefs.getBoolean(Constants.HAS_USER_SIGNED_IN, null == true)
    }
}

