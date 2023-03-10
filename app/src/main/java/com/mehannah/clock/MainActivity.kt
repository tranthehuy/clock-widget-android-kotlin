package com.mehannah.clock

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.mehannah.clock.common.*
import com.mehannah.clock.pagers.*

private val tabIcons = intArrayOf(
    android.R.drawable.ic_menu_preferences,
    android.R.drawable.ic_menu_compass,
    android.R.drawable.ic_menu_share
)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Settings.init(this, getString(R.string.app_settings_name))

        val pager = findViewById<ViewPager>(R.id.pager)
        pager.adapter = CollectionPagerAdapter(supportFragmentManager)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.setupWithViewPager(pager)

        tabLayout.getTabAt(0)?.setIcon(tabIcons[0]);
        tabLayout.getTabAt(1)?.setIcon(tabIcons[1]);
        tabLayout.getTabAt(2)?.setIcon(tabIcons[2]);
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.right_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.updateMenuBtn) {
            val spFontSize = findViewById<Spinner>(R.id.spFontSize)
            Settings.saveString(FONT_SIZE, spFontSize.selectedItem.toString())

            val spDateFormat = findViewById<Spinner>(R.id.spDateFormat)
            Settings.saveString(DATE_FORMAT, spDateFormat.selectedItem.toString())

            val spTimeFormat = findViewById<Spinner>(R.id.spTimeFormat)
            Settings.saveString(TIME_FORMAT, spTimeFormat.selectedItem.toString())

            val spTextStyle = findViewById<Spinner>(R.id.spTextStyle)
            Settings.saveString(TEXT_STYLE, spTextStyle.selectedItem.toString())

            this.updateWidgets()

            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(homeIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateWidgets() {
        val intent = Intent(this, ClockWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
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