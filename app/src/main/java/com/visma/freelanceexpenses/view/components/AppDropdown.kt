package com.visma.freelanceexpenses.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.visma.freelanceexpenses.R

@Composable
fun AppDropdown(
    itemsList: List<String>,
    selectedPosition : Int,
    onItemClick: (Int) -> Unit,
    modifier: Modifier) {
    var isDropDownExpanded by remember {
        mutableStateOf(false)
    }

    Box(modifier= modifier.border(
        width = 1.dp,
        Color.Black,
        shape = RoundedCornerShape(
            topStart = 5.dp,
            topEnd = 5.dp,
            bottomEnd = 5.dp,
            bottomStart = 5.dp
        ))
        .requiredHeight(30.dp)) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    isDropDownExpanded = true
                }
        ) {
            Text(itemsList[selectedPosition],
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_drop_down),
                contentDescription = ""
            )
        }
        DropdownMenu(
            expanded = isDropDownExpanded,
            onDismissRequest = {
                isDropDownExpanded = false
            }, modifier = modifier.background(Color.White)
        ) {
            itemsList.forEachIndexed { index, item ->
                DropdownMenuItem(text = {
                    Text(text = item)
                },
                    onClick = {
                        isDropDownExpanded = false
                        onItemClick(index)
                    })
            }
        }
    }
}