package com.example.todoapp.view.state

import com.example.todoapp.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class TodoListState(
    val todoList: Flow<List<TodoEntity>> = flow{ emptyList<TodoEntity>() },
    val error: String = ""
)
