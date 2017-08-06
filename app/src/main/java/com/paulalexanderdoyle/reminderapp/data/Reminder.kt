package com.paulalexanderdoyle.reminderapp.data

import java.util.*

class Reminder(var title:String, var dueDate: Date?) {
    var creationDate: Date? = null
    var completedDate: Date? = null
}