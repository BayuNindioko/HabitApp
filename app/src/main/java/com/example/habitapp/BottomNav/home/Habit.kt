package com.example.habitapp.BottomNav.home

data class Habit(
    val title:String ?=null,
    val minutesFocus:Int ?=null,
    val priorityLevel : String ?= null,
    val startTime : String?=null
)