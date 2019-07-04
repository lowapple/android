package io.lowapple.sparta.git.app

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object Preference {
    fun instance(context: Context): SharedPreferences {
        return context.getSharedPreferences("pref", MODE_PRIVATE)
    }
}