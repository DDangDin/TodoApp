package com.example.todoapp.view.state

import com.example.todoapp.data.local.entity.TodoEntity

data class TodoListState(
    val todoList: List<TodoEntity> = emptyList(),
    val loading: Boolean = false,
    val error: String = ""
)