package com.example.todoapp.view.todo_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoapp.R
import com.example.todoapp.util.UiEvent
import com.example.todoapp.view.common.TextTopBar
import com.example.todoapp.view.viewmodel.TodoViewModel

@Composable
fun TodoListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TodoViewModel = hiltViewModel()
) {

    val todos = viewModel.todos.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()
    val isLoading = rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(TodoListEvent.OnUndoDeleteClick)
                    }
                }
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.ListLoading -> isLoading.value = event.isLoading
                else -> Unit
            }
        }
    }

//    if (state.error.isNotBlank()) {
//        Text(
//            text = state.error,
//            color = MaterialTheme.colors.error,
//            textAlign = TextAlign.Center,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 20.dp)
//                .align(Alignment.Center)
//        )
//    }
//    if(state.isLoading) {
//        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(TodoListEvent.OnAddTodoClick)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "추가"
                )
            }

        },
        floatingActionButtonPosition = FabPosition.Center,
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
                text = R.string.pageName_todoList
            )
            if (!isLoading.value) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    items(todos.value) { todo ->
                        TodoItem(
                            todo = todo,
                            onEvent = viewModel::onEvent,    // !!
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onEvent(TodoListEvent.OnTodoClick(todo))
                                },
                        )

                    }

                }
            } else {
                Box(modifier = Modifier.fillMaxSize()){
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }

}