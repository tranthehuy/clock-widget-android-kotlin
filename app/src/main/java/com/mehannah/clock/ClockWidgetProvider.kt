package com.mehannah.clock

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews
import com.mehannah.clock.constants.DATE_FORMAT
import com.mehannah.clock.constants.FONT_SIZE
import com.mehannah.clock.constants.TEXT_STYLE
import com.mehannah.clock.constants.TIME_FORMAT
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
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

        views.setTextColor(R.id.clock, Color.parseColor(textStyle))
        views.setTextColor(R.id.date_clock, Color.parseColor(textStyle))
        views.setTextColor(R.id.tvQuote, Color.parseColor(textStyle))
        views.setTextColor(R.id.tvLunarDate, Color.parseColor(textStyle))

        val textStyleString = AppSettings.getString(TEXT_STYLE)
        if (textStyleString.contains("Background", true)) {
            if (textStyleString.contains("White Background", true)) {
                views.setInt(R.id.widget, "setBackgroundColor", Color.parseColor("#CCFFFFFF"));
            }
            if (textStyleString.contains("Black Background", true)) {
                views.setInt(R.id.widget, "setBackgroundColor", Color.parseColor("#CC000000"));
            }
        } else {
            views.setInt(R.id.widget, "setBackgroundColor", Color.TRANSPARENT);
        }

        views.setTextViewTextSize(R.id.clock,TypedValue.COMPLEX_UNIT_SP,
            fontSize.toFloat()
        )

        try {
            val dateSetting = AppSettings.getString(DATE_FORMAT)
            val isHadLunarCalendar = dateSetting.contains("+")
            val isHadLunarIcon = dateSetting.contains("Icon")
            if (isHadLunarCalendar) {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH) + 1
                val day = c.get(Calendar.DAY_OF_MONTH)
                val dateValues = LunarCalendar.convertSolar2Lunar(day, month, year, "7".toDouble());

                val localDateTime = LocalDateTime.of(dateValues[2], dateValues[1], dateValues[0],0,0,0)
                val lunarFormat = dateFormat
                    .replace("E", "")
                    .replace(",", "")
                val formatter = DateTimeFormatter.ofPattern(lunarFormat)

                val prefix = if (isHadLunarIcon) "🌜" else "Âm lịch "

                val lunarDateStr = prefix + formatter.format(localDateTime)
                views.setTextViewText(R.id.tvLunarDate, lunarDateStr)
                views.setViewVisibility(R.id.tvLunarDate, View.VISIBLE)
            } else {
                views.setViewVisibility(R.id.tvLunarDate, View.GONE)
            }
        } catch(err: Exception) {
            Log.d("app_debug" , err.toString())
            views.setViewVisibility(R.id.tvLunarDate, View.GONE)
        }

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
