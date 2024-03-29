package com.example.todoapp.view.todo_list

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoapp.R
import com.example.todoapp.util.UiEvent
import com.example.todoapp.view.common.TextTopBar
import com.example.todoapp.view.viewmodel.TodoViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun TodoListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    onCompleteTodoListClick: () -> Unit,
    viewModel: TodoViewModel = hiltViewModel()
) {

    val incompleteTodos = viewModel.incompleteTodos.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()
    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        if (event.action == "취소") {
                            viewModel.onEvent(TodoListEvent.OnUndoDeleteClick)
                        } else {
                            viewModel.onEvent(TodoListEvent.OnCompleteTodoListClick)
                        }
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
                .padding(start = 13.dp, end = 13.dp, top = 13.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, top = 25.dp, bottom = 25.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextTopBar(text = R.string.pageName_todoList)
                IconButton(onClick = { onCompleteTodoListClick() }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_playlist_add_check_24),
                        contentDescription = stringResource(id = R.string.pageName_completeTodo),
                        tint = MaterialTheme.colors.primary
                    )
                }
            }

            if (!isLoading.value) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    contentPadding = PaddingValues(bottom = 13.dp)
                ) {
                    items(incompleteTodos.value, key = { it.id!! }) { todo ->
                        TodoItem(
                            todo = todo,
                            onEvent = viewModel::onEvent,    // !!
                            modifier = Modifier
                                .animateItemPlacement(
                                    animationSpec = tween(
                                        durationMillis = 500,
                                        easing = LinearOutSlowInEasing,
                                    )
                                )
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onEvent(TodoListEvent.OnTodoClick(todo))
                                },
                            onDelete = {
                                viewModel.onEvent(TodoListEvent.OnDeleteTodoClick(todo))
//                                    visibleAnimation = false
                            }
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }

}