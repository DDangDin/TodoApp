package com.example.todoapp.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.local.entity.TodoEntity
import com.example.todoapp.domain.repository.TodoRepository
import com.example.todoapp.util.Routes
import com.example.todoapp.util.UiEvent
import com.example.todoapp.view.todo_list.TodoListEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
//    private val todoUseCases: TodoUseCases    // 추후에 이 기능 사용해보기
    private val repository: TodoRepository
) : ViewModel() {

//    val todosTemp = repository.getTodosTemp()

    var todos: Flow<List<TodoEntity>>
        private set

    var completeTodos: Flow<List<TodoEntity>>
        private set
    var incompleteTodos: Flow<List<TodoEntity>>
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedTodo: TodoEntity? = null

    init {
        sendUiEvent(UiEvent.ListLoading(true))
        todos = repository.getTodos()
        sendUiEvent(UiEvent.ListLoading(false))
        completeTodos = todos.map { it.filter { it.isDone } }
        incompleteTodos = todos.map { it.filterNot { it.isDone } }

//        viewModelScope.launch {
//            for (i in 1..30) {
//                repository.insertTodo(
//                    TodoEntity(
//                        title = "#$i Test Title",
//                        content = "$i test",
//                        id = i,
//                        isDone = false,
//                        timestamp = 10 * i.toLong()
//                    )
//                )
//            }
//        }
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

                    if (!event.todo.isDone) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "TODO 완료! (완료한 TODO는 저장됩니다)",
                                action = "이동"
                            )
                        )
                    } else {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "TODO 복구!"
                            )
                        )
                    }

                    Log.d("save_check_data", event.isDone.toString())
                }
            }
            is TodoListEvent.OnCompleteTodoListClick -> {
                // -> 이 부분은 SnackBar에서 action버튼(이동 버튼) 눌렀을 때만 사용
                
                // CompleteTodoList 아이콘 클릭했을 때 이 부분 사용안한 이유 ->
                // UiEvent로 처리하면 CompleteTodoList로 이동할 때
                // SnackBar가 사라질때까지 기다려야함
                // 그래서 바로 고차함수를 통해 직접적으로 NavigationGraph에서 navigate 해줬음
                sendUiEvent(UiEvent.Navigate(Routes.COMPLETE_TODO_LIST))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

//    fun filterTodos(currentTodo: TodoEntity, todoList: List<TodoEntity>): Boolean {
//
//        return true
//    }
}