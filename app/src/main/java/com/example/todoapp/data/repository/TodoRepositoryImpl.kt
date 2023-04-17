package com.example.todoapp.data.repository

import com.example.todoapp.data.local.TodoDao
import com.example.todoapp.data.local.entity.TodoEntity
import com.example.todoapp.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow


class TodoRepositoryImpl(
    private val dao: TodoDao
): TodoRepository {

    override fun getTodos(): Flow<List<TodoEntity>> {
        return dao.getTodos()
    }

    override suspend fun getTodoById(id: Int): TodoEntity? {
        return dao.getTodoById(id)
    }

    override suspend fun insertTodo(todo: TodoEntity) {
        dao.insertTodo(todo)
    }

    override suspend fun deleteTodo(todo: TodoEntity) {
        dao.deleteTodo(todo)
    }
}