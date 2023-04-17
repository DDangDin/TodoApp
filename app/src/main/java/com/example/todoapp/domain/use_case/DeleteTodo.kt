package com.example.todoapp.domain.use_case

import com.example.todoapp.data.local.entity.TodoEntity
import com.example.todoapp.domain.repository.TodoRepository

class DeleteTodo(
    private val repository: TodoRepository
) {

    suspend operator fun invoke(todo: TodoEntity) {
        repository.deleteTodo(todo)
    }
}