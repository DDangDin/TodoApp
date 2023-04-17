package com.example.todoapp.domain.repository

import com.example.todoapp.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow


interface TodoRepository {

    fun getTodos(): Flow<List<TodoEntity>>

    suspend fun getTodoById(id: Int): TodoEntity?

    suspend fun insertTodo(todo: TodoEntity)

    suspend fun deleteTodo(todo: TodoEntity)
}