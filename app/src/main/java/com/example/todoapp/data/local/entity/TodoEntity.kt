package com.example.todoapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoEntity(
    val title: String,
    val content: String? = null,
    val timestamp: Long? = null,
    val isDone: Boolean,
    @PrimaryKey val id: Int? = null
) {
//    companion object {
//        val noteColors = listOf()
//    }
}
