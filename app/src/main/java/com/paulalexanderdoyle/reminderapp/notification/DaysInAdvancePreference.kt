package com.paulalexanderdoyle.reminderapp.notification

import android.content.Context
import android.support.v7.preference.EditTextPreference
import android.util.AttributeSet
import com.paulalexanderdoyle.reminderapp.R

class DaysInAdvancePreference : EditTextPreference {
    @Suppress("unused")
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,
            android.support.v7.preference.R.attr.editTextPreferenceStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            this(context, attrs, defStyleAttr, 0)

    @Suppress("unused")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes)

    override fun setText(text: String?) {
        super.setText(text)
        val intVal = text?.toIntOrNull()
        if (intVal != null) {
            val daysText = when {
                (intVal > 1) -> context.getString(R.string.x_days, intVal)
                else -> context.getString(R.string.day)
            }
            summary = context.getString(R.string.notify_upcoming_description, daysText)
        }
    }

    override fun getDialogLayoutResource(): Int {
        return R.layout.number_edittext_preference
    }
}