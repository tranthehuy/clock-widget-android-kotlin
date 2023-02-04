package com.mehannah.clock

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.mehannah.clock.Utils.Companion.getSizeIndex


class MainActivity : AppCompatActivity() {
    private lateinit var appRef: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appRef = getSharedPreferences(getString(R.string.app_settings_name), MODE_PRIVATE)

        initUpdateButton();
    }

    private fun initUpdateButton () {
        val updateButton = findViewById<Button>(R.id.btnUpdate)

        appRef.getString("font_size", "Large")
            ?.let {
                val selectedIndex = getSizeIndex(it)
                findViewById<Spinner>(R.id.spFontSize).setSelection(selectedIndex)
            }

        updateButton.setOnClickListener(View.OnClickListener {
            val spFontSize = findViewById<Spinner>(R.id.spFontSize)
            appRef.edit().putString("font_size", spFontSize.selectedItem.toString()).commit()
            this.updateWidgets()
        })
    }

    private fun updateWidgets() {
        val intent = Intent(this, ClockWidgetProvider::class.java)
        intent.action = "android.appwidget.action.APPWIDGET_UPDATE"
        val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(
            ComponentName(
                application,
                ClockWidgetProvider::class.java
            )
        )
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)
    }
}