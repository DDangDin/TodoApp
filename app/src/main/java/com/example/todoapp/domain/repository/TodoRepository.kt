package com.example.todoapp.domain.repository

import com.example.todoapp.data.local.entity.TodoEntity
import com.example.todoapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


interface TodoRepository {

    fun getTodos(): Flow<List<TodoEntity>>

    suspend fun getTodoById(id: Int): TodoEntity?

    suspend fun insertTodo(todo: TodoEntity)

    suspend fun deleteTodo(todo: TodoEntity)
}