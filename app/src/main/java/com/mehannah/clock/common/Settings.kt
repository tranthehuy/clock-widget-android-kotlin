package com.mehannah.clock.common

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class Settings {
    companion object {
        private var ref: SharedPreferences? = null

        fun init (context: Context, name: String) {
            if (ref == null) {
                ref = context.getSharedPreferences(name, AppCompatActivity.MODE_PRIVATE)
            }
        }

        fun getString(key: String, defaultValue: String = ""): String {
            return ref?.getString(key, defaultValue) ?: defaultValue
        }

        fun saveString(key: String, value: String): Boolean {
            return try {
                ref?.edit()?.putString(key, value)?.commit()
                true
            } catch (err: Error) {
                false
            }
        }
    }
}