package com.example.messengerAndroid.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.messengerAndroid.Constants.APP_PREFERENCES
import com.example.messengerAndroid.Constants.USER_NAME_KEY


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

    fun setName(value: String) = prefs.edit { it.putString(USER_NAME_KEY, value) }

    fun getName() : String = prefs.getString(USER_NAME_KEY,"")?: ""

    fun clearPrefs() {
        prefs.edit().clear().apply()
    }
}