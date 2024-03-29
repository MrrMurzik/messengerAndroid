package com.example.messengerAndroid.app.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.messengerAndroid.app.Constants.APP_PREFERENCES
import com.example.messengerAndroid.app.Constants.ID_KEY
import com.example.messengerAndroid.app.Constants.PHOTO_KEY
import com.example.messengerAndroid.app.Constants.TOKEN_KEY
import com.example.messengerAndroid.app.Constants.USER_NAME_KEY


object SharedPreferencesHelper {

    private lateinit var prefs: SharedPreferences

    fun init(context: Context?) {
        prefs = context?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE) ?: return
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    fun setToken(value: String) = prefs.edit { it.putString(TOKEN_KEY, value)}

    fun getToken() : String = prefs.getString(TOKEN_KEY,"")?: ""

    fun setNetworkId(value: String) = prefs.edit { it.putString(ID_KEY, value)}

    fun getNetworkId() : String = prefs.getString(ID_KEY,"")?: ""

    fun setPhoto(value: String) = prefs.edit { it.putString(PHOTO_KEY, value)}

    fun getPhoto(): String? = prefs.getString(PHOTO_KEY, null)

    fun setName(value: String) = prefs.edit { it.putString(USER_NAME_KEY, value)}

    fun getName(): String? = prefs.getString(USER_NAME_KEY, null)

    fun clearPrefs() {
        prefs.edit().clear().apply()
    }


}