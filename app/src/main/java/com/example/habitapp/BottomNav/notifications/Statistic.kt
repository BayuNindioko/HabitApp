package com.example.habitapp.BottomNav.notifications

import com.google.firebase.Timestamp

data class Statistic (
    var habitId:String ?=null,
    val title:String ?=null,
    val added: Timestamp ?= null,
    val averageDuration: Long ?= null,
    val lastUsage: Timestamp ?= null,
    val totalUsage: Long ?= null,
)