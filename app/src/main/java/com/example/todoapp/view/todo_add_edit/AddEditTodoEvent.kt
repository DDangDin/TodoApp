package com.example.todoapp.view.todo_add_edit

sealed class AddEditTodoEvent {
    data class OnTitleChange(val title: String): AddEditTodoEvent()
    data class OnContentChange(val content: String): AddEditTodoEvent()
    object OnSaveTodoClick: AddEditTodoEvent()
}
