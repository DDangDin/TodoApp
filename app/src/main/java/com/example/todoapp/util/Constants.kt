package com.example.todoapp.util

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.todoapp.data.local.entity.TodoEntity

object Constants {
    val sampleTodo = TodoEntity(
        content = "sample content",
        isDone = true,
        timestamp = 10L,
        title = "Sample Title"
    )
}