package com.noice.todo_app.data.model


enum class Status{
    Pending,
    Completed,
    Expired
}

enum class Priority{
    Low,
    Medium,
    High
}

data class Task(
    var title: String = "",
    var status: Int = Status.Pending.ordinal,
    val listId: String,
    val taskId: String,
    val taskCreatedOn:String,
    var priority: Int = Priority.Low.ordinal,
    var dueDate:String? = null,
    var deferrable:Boolean? = null,
    var reminder:String? = null,
    var repeat:Boolean = false,
    var repeatData:String? = null,
    var taskCompletedOn:String? = null

)
