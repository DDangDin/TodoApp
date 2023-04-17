package com.example.todoapp.view.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.local.entity.TodoEntity
import com.example.todoapp.domain.repository.TodoRepository
import com.example.todoapp.util.UiEvent
import com.example.todoapp.view.todo_add_edit.AddEditTodoEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val repository: TodoRepository,
    // 상태 변수를 포함하는 키-값 개체의 종류, 뷰 모델 상태를 복원 하는데 사용할 수 있음
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var todo by mutableStateOf<TodoEntity?>(null)
        private set // only change this viewmodel

    var title by mutableStateOf("")
        private set

    var content by mutableStateOf("")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val todoId = savedStateHandle.get<Int>("todoId")!!
        if (todoId != -1) { // 해당 id의 데이터가 아직 DB에 저장 X -> -1 값을 가짐
            viewModelScope.launch {
                repository.getTodoById(todoId)?.let { todo ->
                    title = todo.title
                    content = todo.content ?: ""
                    this@AddEditTodoViewModel.todo = todo
                }
            }
        }
    }

    fun onEvent(event: AddEditTodoEvent) {
        when(event) {
            is AddEditTodoEvent.OnTitleChange -> {
                title = event.title
            }
            is AddEditTodoEvent.OnContentChange -> {
                content = event.content
            }
            is AddEditTodoEvent.OnSaveTodoClick -> {
                viewModelScope.launch {
                    if(title.isBlank()) {
                        sendUiEvent(UiEvent.ShowSnackbar(
                            message = "할일을 적어주세요"
                        ))
                        return@launch
                    }
                    repository.insertTodo(
                        TodoEntity(
                            title = title,
                            content = content,
                            isDone = todo?.isDone ?: false,
                            id = todo?.id,
                            timestamp = todo?.timestamp ?: 0
                        )
                    )
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}
