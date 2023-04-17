package com.example.todoapp.view.todo_add_edit

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoapp.R
import com.example.todoapp.util.UiEvent
import com.example.todoapp.view.common.TextTopBar
import com.example.todoapp.view.viewmodel.AddEditTodoViewModel

@Composable
fun AddEditTodoScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditTodoViewModel = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(AddEditTodoEvent.OnSaveTodoClick)
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "저장"
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(13.dp)
        ) {
            TextTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 25.dp, top = 25.dp, bottom = 25.dp),
                text = R.string.pageName_addTodo
            )
            Spacer(modifier = Modifier.height(13.dp))
            TextField(
                value = viewModel.title,
                onValueChange = {
                    viewModel.onEvent(AddEditTodoEvent.OnTitleChange(it))
                },
                placeholder = { Text(text = stringResource(id = R.string.addTodo_textField_title)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = viewModel.content,
                onValueChange = {
                    viewModel.onEvent(AddEditTodoEvent.OnContentChange(it))
                },
                placeholder = { Text(text = stringResource(id = R.string.addTodo_textField_content)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 10
            )
        }
    }
}