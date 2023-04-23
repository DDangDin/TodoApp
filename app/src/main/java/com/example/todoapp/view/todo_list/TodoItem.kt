package com.example.todoapp.view.todo_list

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.data.local.entity.TodoEntity
import com.example.todoapp.ui.theme.TodoAppTheme
import com.example.todoapp.util.Constants.sampleTodo
import kotlinx.coroutines.delay

@Composable
fun TodoItem(
//    @StringRes title: Int,
//    @DrawableRes checkIcon: Int,
    modifier: Modifier = Modifier,
    todo: TodoEntity,
    onEvent: (TodoListEvent) -> Unit,
    onDelete: () -> Unit,
//    enableCheckIcon: Boolean = false
) {
    var isCheck by remember { mutableStateOf(todo.isDone) }

    val checkedIcon = if (todo.isDone) {
        ImageVector.vectorResource(id = R.drawable.img_checked)
    } else {
        ImageVector.vectorResource(id = R.drawable.img_check)
    }

    val checkedTextDeco = if (todo.isDone) {
        TextDecoration.LineThrough
    } else {
        TextDecoration.None
    }

    val checkedTextColor = if (todo.isDone) {
        Color.LightGray
    } else {
        Color.Black
    }

    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .clickable {
                            isCheck = !todo.isDone
                            onEvent(TodoListEvent.OnDoneChange(todo, isCheck))
                        },
                    imageVector = checkedIcon,
                    contentDescription = "isCheck"
                )
                Text(
                    modifier = Modifier.padding(18
                        .dp),
                    text = todo.title,
                    textDecoration = checkedTextDeco,
                    color = checkedTextColor,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Start
                )
            }
            IconButton(
                onClick = {
//                        onEvent(TodoListEvent.OnDeleteTodoClick(todo))
                    onDelete()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryListItemPreview() {
    TodoAppTheme() {
        TodoItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            todo = sampleTodo,
            onEvent = {},
            onDelete = {}
        )
    }
}