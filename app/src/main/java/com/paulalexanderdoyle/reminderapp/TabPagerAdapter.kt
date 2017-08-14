package com.paulalexanderdoyle.reminderapp

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class TabPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return ActiveRemindersFragment()
            1 -> return CompletedRemindersFragment()
            else -> return null
        }
    }

    override fun getCount(): Int {
        return 2
    }
}