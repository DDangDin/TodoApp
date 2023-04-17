package com.example.todoapp.view.compose

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.R
import com.example.todoapp.ui.theme.TodoAppTheme

@Composable
fun Item(
    @StringRes title: Int,
    @DrawableRes checkImg: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .clickable {  },
                imageVector = ImageVector.vectorResource(id = checkImg),
                contentDescription = "isCheck"
            )
            Text(
                modifier = Modifier.padding(15.dp),
                text = stringResource(id = title),
                fontSize = 13.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF03DAC5)
@Composable
fun CategoryListItemPreview() {
    TodoAppTheme() {
        Item(
            title = R.string.category_list_item,
            checkImg = R.drawable.img_check,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )
    }
}