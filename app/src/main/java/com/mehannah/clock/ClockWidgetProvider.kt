package com.mehannah.clock

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.TypedValue
import android.widget.RemoteViews
import com.mehannah.clock.constants.*
import kotlin.random.Random.Default.nextInt

class ClockWidgetProvider : AppWidgetProvider() {

    private fun getRandomQuote(quotes: Array<String>): String {
        val randomIndex = nextInt(quotes.size)
        return quotes[randomIndex]
    }

    private fun getMainActivityIntent(context: Context, pendingIntent: PendingIntent): PendingIntent? {
        val configIntent = Intent(context, MainActivity::class.java)
        configIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(context, 0, configIntent, PendingIntent.FLAG_UPDATE_CURRENT  + PendingIntent.FLAG_MUTABLE)
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

        val fontSize = Utils.getOptionValue(
            AppSettings.getString(FONT_SIZE),
            Utils.getJsonResource<Options>(context, R.raw.font_options)
        )

        val dateFormat = Utils.getOptionValue(
            AppSettings.getString(DATE_FORMAT),
            Utils.getJsonResource<Options>(context, R.raw.date_formats)
        )

        val timeFormat = Utils.getOptionValue(
            AppSettings.getString(TIME_FORMAT),
            Utils.getJsonResource<Options>(context, R.raw.time_formats)
        )

        val textStyle = Utils.getOptionValue(
            AppSettings.getString(TEXT_STYLE),
            Utils.getJsonResource<Options>(context, R.raw.text_style)
        )

        val quote = getRandomQuote(context.resources.getStringArray(R.array.quotes))

        views.setTextViewText(R.id.tvQuote, quote)

        views.setCharSequence(R.id.date_clock,"setFormat24Hour", dateFormat);
        views.setCharSequence(R.id.date_clock,"setFormat12Hour", dateFormat);

        views.setCharSequence(R.id.clock,"setFormat24Hour", timeFormat);
        views.setCharSequence(R.id.clock,"setFormat12Hour", timeFormat);

        views.setTextColor(R.id.clock, Color.parseColor(textStyle));
        views.setTextColor(R.id.date_clock, Color.parseColor(textStyle));
        views.setTextColor(R.id.tvQuote, Color.parseColor(textStyle));

        views.setTextViewTextSize(R.id.clock,TypedValue.COMPLEX_UNIT_SP,
            fontSize.toFloat()
        )

        views.setOnClickPendingIntent(R.id.tvQuote, pendingIntent)

        val configPendingIntent = getMainActivityIntent(context, pendingIntent)
        views.setOnClickPendingIntent(R.id.clock, configPendingIntent)

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
