package com.mehannah.clock

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.widget.RemoteViews
import kotlin.random.Random.Default.nextInt

class ClockWidgetProvider : AppWidgetProvider() {

    private fun getRandomQuote(quotes: Array<String>): String {
        val randomIndex = nextInt(quotes.size)
        return quotes[randomIndex]
    }

    private fun createViews(
        context: Context,
        pendingIntent: PendingIntent
    ): RemoteViews {
        val layoutId: Int = R.layout.clock_layout
        val views = RemoteViews(
            context.packageName,
            layoutId
        )

        val fontSize = Utils.getSize(AppSettings.getString("font_size", "Large"))

        val quote = getRandomQuote(context.resources.getStringArray(R.array.quotes))

        views.setTextViewText(R.id.tvQuote, quote)

        views.setTextViewTextSize(R.id.clock,TypedValue.COMPLEX_UNIT_SP,
            fontSize.toFloat()
        )

        views.setOnClickPendingIntent(R.id.tvQuote, pendingIntent)

        return views;
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        AppSettings.init(context, context.getString(R.string.app_settings_name))

        // Get all ids
        val thisWidget = ComponentName(
            context,
            ClockWidgetProvider::class.java
        )

        val ids = appWidgetManager.getAppWidgetIds(thisWidget)

        val intent = Intent(context, ClockWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_MUTABLE)

        appWidgetIds.forEach { appWidgetId ->
            appWidgetManager.updateAppWidget(appWidgetId, createViews(context, pendingIntent))
        }
    }
}
