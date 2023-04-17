package com.example.todoapp.view.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.R

@Composable
fun TodoList(
    modifier: Modifier = Modifier,
    items: List<Int>
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        items(items) {item ->
            Item(
                modifier = Modifier.clickable {  },
                title = item,
                checkImg = R.drawable.img_check
            )
        }

    }
}