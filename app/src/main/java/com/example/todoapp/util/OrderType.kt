package com.example.todoapp.util



sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
