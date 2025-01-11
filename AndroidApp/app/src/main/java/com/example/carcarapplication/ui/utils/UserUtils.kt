package com.example.carcarapplication.ui.utils

import com.example.carcarapplication.data_classes.Group
import com.example.carcarapplication.data_classes.User

fun isAdmin(group: Group, user: User): Boolean {
    return group.admin.userID == user.userID
}

//This probably has to be rewritten once the connection to the DB has been established
fun promoteToAdmin(group: Group, newAdmin: User): Group{
    require(group.members.contains(newAdmin)) {
        "User must be member of the group."
    }
    return group.copy(
        admin = newAdmin
    )
}

fun removeUser(group: Group, user: User): Boolean{
    TODO()
}