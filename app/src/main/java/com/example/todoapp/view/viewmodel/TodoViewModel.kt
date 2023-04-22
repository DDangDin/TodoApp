package com.example.todoapp.view.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.local.entity.TodoEntity
import com.example.todoapp.domain.repository.TodoRepository
import com.example.todoapp.util.Resource
import com.example.todoapp.util.Routes
import com.example.todoapp.util.UiEvent
import com.example.todoapp.view.state.TodoListState
import com.example.todoapp.view.todo_list.TodoListEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
//    private val todoUseCases: TodoUseCases    // 추후에 이 기능 사용해보기
    private val repository: TodoRepository
) : ViewModel() {

//    val todos = repository.getTodos()

    var todos: Flow<List<TodoEntity>> = flow { emptyList<TodoEntity>() }
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedTodo: TodoEntity? = null

    init {
        sendUiEvent(UiEvent.ListLoading(true))
        todos = repository.getTodos()
        sendUiEvent(UiEvent.ListLoading(false))
    }

    fun onEvent(event: TodoListEvent) {
        when (event) {
            is TodoListEvent.OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo.id}"))
            }
            is TodoListEvent.OnAddTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))
            }
            is TodoListEvent.OnUndoDeleteClick -> {
                deletedTodo?.let { todo ->
                    viewModelScope.launch {
                        repository.insertTodo(todo)
                    }
                }
            }
            is TodoListEvent.OnDeleteTodoClick -> {
                viewModelScope.launch {
                    deletedTodo = event.todo
                    repository.deleteTodo(event.todo)
                    sendUiEvent(
                        UiEvent.ShowSnackbar(
                            message = "삭제 되었습니다",
                            action = "취소"
                        )
                    )
                }
            }
            is TodoListEvent.OnDoneChange -> {
                viewModelScope.launch {
                    repository.insertTodo(
                        event.todo.copy(
                            isDone = event.isDone
                        )
                    )
                    Log.d("save_check_data", event.isDone.toString())
                }
            }
            is TodoListEvent.OnTodoComplete -> {

            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}