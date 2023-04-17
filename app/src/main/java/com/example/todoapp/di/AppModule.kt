package com.example.todoapp.di

import android.app.Application
import androidx.room.Room
import com.example.todoapp.data.local.TodoDao
import com.example.todoapp.data.local.TodoDatabase
import com.example.todoapp.data.repository.TodoRepositoryImpl
import com.example.todoapp.domain.repository.TodoRepository
import com.example.todoapp.domain.use_case.DeleteTodo
import com.example.todoapp.domain.use_case.GetTodos
import com.example.todoapp.domain.use_case.TodoUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(app: Application): TodoDatabase {
        return Room.databaseBuilder(
            app,
            TodoDatabase::class.java,
            TodoDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(db: TodoDatabase): TodoRepository {
        return TodoRepositoryImpl(db.todoDao)
    }

    // 만약 가짜 구현 리포지토리를 생성해서
    // 테스트를 해보고 싶으면 테스트 모듈하나를 추가한다 -> 리포지토리 인터페이스를 만든 이유
    // Ex)
//    @Provides
//    @Singleton
//    fun provideTodoRepository(db: TodoDatabase): TodoRepository {
//        return FakeTodoRepositoryImpl(db.todoDao)
//    }

    @Provides
    @Singleton
    fun provideTodoUseCases(repository: TodoRepository): TodoUseCases {
        return TodoUseCases(
            getTodos = GetTodos(repository),
            deleteTodo = DeleteTodo(repository)
        )
    }
}