package com.noice.todo_app.data.model

data class User(
    val Id:String,
    var Name:String,
    var Email:String,
    val CreatedOn:String,
    var lastOnline:String,
    var password:String,
    var phoneNumber:String
)
