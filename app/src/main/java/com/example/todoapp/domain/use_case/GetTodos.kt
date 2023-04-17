package com.example.todoapp.domain.use_case

import com.example.todoapp.data.local.entity.TodoEntity
import com.example.todoapp.domain.repository.TodoRepository
import com.example.todoapp.util.OrderType
import com.example.todoapp.util.TodoOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTodos(
    private val repository: TodoRepository
) {

    operator fun invoke(
        todoOrder: TodoOrder = TodoOrder.Date(OrderType.Descending)
    ): Flow<List<TodoEntity>> {
        return repository.getTodos().map { todos ->
            when(todoOrder.orderType) {
                is OrderType.Ascending -> {
                    when(todoOrder) {
                        is TodoOrder.Title -> todos.sortedBy { it.title.lowercase() }
                        is TodoOrder.Date -> todos.sortedBy { it.timestamp }
                    }
                }
                is OrderType.Descending -> {
                    when(todoOrder) {
                        is TodoOrder.Title -> todos.sortedByDescending { it.title.lowercase() }
                        is TodoOrder.Date -> todos.sortedByDescending { it.timestamp }
                    }
                }
            }
        }
    }
}