package com.mehannah.clock

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class ClockWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            val layoutId: Int = R.layout.clock_layout
            val views = RemoteViews(
                context.packageName,
                layoutId
            )
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}