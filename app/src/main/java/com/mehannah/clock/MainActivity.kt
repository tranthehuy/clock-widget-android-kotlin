package com.mehannah.clock

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.mehannah.clock.constants.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppSettings.init(this, getString(R.string.app_settings_name))

        initOption(FONT_SIZE, R.raw.font_options, R.id.spFontSize)
        initOption(DATE_FORMAT, R.raw.date_formats, R.id.spDateFormat)
        initOption(TIME_FORMAT, R.raw.time_formats, R.id.spTimeFormat)
        initOption(TEXT_STYLE, R.raw.text_style, R.id.spTextStyle)

        initUpdateButton()
    }

    private fun initOption(name: String, dataId: Int, componentId: Int) {
        val spinner = findViewById<Spinner>(componentId)

        val options =
            Utils.getJsonResource<Options>(this, dataId)
        val items = options.items.map { it -> it?.name ?: "" }
        if (items.isEmpty()) return;

        val spinnerArrayAdapter = Utils.createItemList(items, this)
        spinner.adapter = spinnerArrayAdapter

        val currentValue = AppSettings.getString(name, items[0])
        items.indexOf(currentValue)?.let{
            spinner.setSelection(it)
        }
    }

    private fun initUpdateButton () {
        val updateButton = findViewById<Button>(R.id.btnUpdate)

        updateButton.setOnClickListener(View.OnClickListener {
            val spFontSize = findViewById<Spinner>(R.id.spFontSize)
            AppSettings.saveString(FONT_SIZE, spFontSize.selectedItem.toString())

            val spDateFormat = findViewById<Spinner>(R.id.spDateFormat)
            AppSettings.saveString(DATE_FORMAT, spDateFormat.selectedItem.toString())

            val spTimeFormat = findViewById<Spinner>(R.id.spTimeFormat)
            AppSettings.saveString(TIME_FORMAT, spTimeFormat.selectedItem.toString())

            val spTextStyle = findViewById<Spinner>(R.id.spTextStyle)
            AppSettings.saveString(TEXT_STYLE, spTextStyle.selectedItem.toString())

            this.updateWidgets()

            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(homeIntent)
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