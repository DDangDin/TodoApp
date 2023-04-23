package com.example.todoapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.example.todoapp.util.Routes
import com.example.todoapp.view.todo_add_edit.AddEditTodoScreen
import com.example.todoapp.view.todo_list.CompleteTodoList
import com.example.todoapp.view.todo_list.TodoListScreen

@Composable
fun NavigationGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.TODO_LIST
    ) {
        composable(route = Routes.TODO_LIST) {
            TodoListScreen(
                onNavigate = { navController.navigate(it.route) },
                onCompleteTodoListClick = { navController.navigate(Routes.COMPLETE_TODO_LIST) }
            )
        }
        composable(
            route = Routes.ADD_EDIT_TODO + "?todoId={todoId}",
            arguments = listOf(
                navArgument(name = "todoId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            AddEditTodoScreen(onPopBackStack = {
                navController.popBackStack()
            })
        }
        composable(route = Routes.COMPLETE_TODO_LIST) {
            CompleteTodoList(
                onNavigate = { navController.navigate(it.route) },
                onPopBackStack = { navController.popBackStack() }
            )
        }
    }
}