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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppSettings.init(this, getString(R.string.app_settings_name))

        initUpdateButton();
    }

    private fun initUpdateButton () {
        val updateButton = findViewById<Button>(R.id.btnUpdate)

        val selectedIndex = getSizeIndex(AppSettings.getString("font_size", "Large"))
        findViewById<Spinner>(R.id.spFontSize).setSelection(selectedIndex)

        updateButton.setOnClickListener(View.OnClickListener {
            val spFontSize = findViewById<Spinner>(R.id.spFontSize)
            AppSettings.saveString("font_size", spFontSize.selectedItem.toString())
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