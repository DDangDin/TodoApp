package com.example.todoapp.view.viewmodel

import androidx.lifecycle.ViewModel
import com.example.todoapp.domain.use_case.TodoUseCases
import javax.inject.Inject

class TodoViewModel @Inject constructor(
    private val todoUseCases: TodoUseCases
): ViewModel() {


}