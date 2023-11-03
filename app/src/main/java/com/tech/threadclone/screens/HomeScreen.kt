package com.tech.threadclone.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.tech.threadclone.R
import com.tech.threadclone.item_view.ThreadItem
import com.tech.threadclone.viewmodels.HomeViewModel

@Composable
fun HomeScreen(navHostController: NavHostController) {

    val homeViewModel: HomeViewModel = viewModel()
    val threadAndUser by homeViewModel.threadsAndUsers.observeAsState(null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.thread_logo_with_background),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                            .size(60.dp)
                            .fillMaxWidth()
                    )
                }
            }
            items(threadAndUser ?: emptyList()) { pair ->
                ThreadItem(
                    threadModel = pair.first, userModel = pair.second, navHostController,
                    FirebaseAuth.getInstance().currentUser?.uid!!
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {

}