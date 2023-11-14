package com.example.habitapp.recomend

data class Habit(
    val title:String ?=null,
    val minutesFocus:Int ?=null,
    val priorityLevel : String ?= null,
    val startTime : String?=null
)