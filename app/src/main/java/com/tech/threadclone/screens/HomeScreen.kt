package com.tech.threadclone.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tech.threadclone.item_view.ThreadItem

@Composable
fun HomeScreen() {
    Column(modifier = Modifier.fillMaxSize()) {

        LazyColumn {
            items(30) {
                ThreadItem()
            }
        }
    }
}