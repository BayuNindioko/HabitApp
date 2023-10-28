package com.example.habitapp.BottomNav.home

data class Habit(
    var docId:String ?=null,
    val title:String ?=null,
    val minutesFocus:Int ?=null,
    val priorityLevel : String ?= null,
    val startTime : String?=null
)