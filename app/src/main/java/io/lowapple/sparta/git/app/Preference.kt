package io.lowapple.sparta.git.app

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class Preference(
    private val context: Context
) {
    var token: String?
        get() {
            return instance(context).getString(TOKEN, null)
        }
        set(value) {
            instance(context).edit().putString(TOKEN, value).apply()
        }

    companion object {
        const val TOKEN = "TOKEN"

        fun instance(context: Context): SharedPreferences {
            return context.getSharedPreferences("pref", MODE_PRIVATE)
        }
    }
}