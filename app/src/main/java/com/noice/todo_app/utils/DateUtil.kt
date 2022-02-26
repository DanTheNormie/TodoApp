package com.noice.todo_app.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    companion object{
        fun getCurrentDate():String{
            val simpleDateFormat= SimpleDateFormat("E dd-MMMM-yyyy h:m:s.S a z")
            val currentDT: String = simpleDateFormat.format(Date())
            return currentDT
        }



    }
}