package com.mehannah.clock.pagers

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.mehannah.clock.constants.ARG_OBJECT
import com.mehannah.clock.constants.TABS
import com.mehannah.clock.fragments.HelpFragment
import com.mehannah.clock.fragments.LayoutFragment
import com.mehannah.clock.fragments.SettingFragment

class CollectionPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int  = 3

    override fun getItem(i: Int): Fragment {
        return when (i) {
            1 -> LayoutFragment()
            2 -> HelpFragment()
            else -> SettingFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return TABS[position]
    }
}