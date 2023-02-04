package com.mehannah.clock

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.TypedValue
import android.widget.RemoteViews

class ClockWidgetProvider : AppWidgetProvider() {

    fun createViews(
        context: Context,
        pref: SharedPreferences
    ): RemoteViews {
        val layoutId: Int = R.layout.clock_layout
        val views = RemoteViews(
            context.packageName,
            layoutId
        )

        pref.getString("font_size", "Large")
            ?.let {
                val fontSize = Utils.getSize(it)
                views.setTextViewTextSize(R.id.clock,TypedValue.COMPLEX_UNIT_SP,
                    fontSize.toFloat()
                )
            }

        return views;
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        val pref = context.getSharedPreferences(context.getString(R.string.app_settings_name),MODE_PRIVATE);

        appWidgetIds.forEach { appWidgetId ->
            appWidgetManager.updateAppWidget(appWidgetId, createViews(context, pref))
        }
    }
}