package com.noice.todo_app.data.model

data class Subtask(
    val id:String,
    var title:String = "",
    var isCompleted:Boolean = false,
    var priority: Int = 0
)
