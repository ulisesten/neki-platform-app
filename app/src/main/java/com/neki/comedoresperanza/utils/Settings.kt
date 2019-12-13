package com.neki.comedoresperanza.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast

class Settings {
    fun closeSession(activity: Activity){

        val prefs = activity.getSharedPreferences("storage", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        Toast.makeText(activity, "Se cerró la sesión", Toast.LENGTH_LONG).show()
    }
}