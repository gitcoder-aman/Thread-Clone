package com.tech.threadclone.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tech.threadclone.item_view.UserItem
import com.tech.threadclone.ui.theme.DarkGray
import com.tech.threadclone.ui.theme.LightGray
import com.tech.threadclone.viewmodels.SearchViewModel

@Composable
fun SearchScreen(screenNavController: NavHostController) {

    val searchViewModel: SearchViewModel = viewModel()
    val userList by searchViewModel.userList.observeAsState()
    var searchText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Search", style = TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                ), textAlign = TextAlign.Start, modifier = Modifier.padding(bottom = 4.dp)
            )
            SearchTextField(searchText, onValueChange = {
                searchText = it
            })
            LazyColumn(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxSize()
            ) {
                if (userList != null && userList!!.isNotEmpty()) {
                    val filterItems =
                        userList?.filter { it.userName.contains(searchText, ignoreCase = true) }
                    items(filterItems ?: emptyList()) { user ->
                        UserItem(
                            userModel = user,
                            screenNavController
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(text: String, onValueChange: (String) -> Unit) {

    TextField(
        value = text,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = "Search", style = TextStyle(
                    fontSize = 14.sp,
                    color = DarkGray,
                    fontWeight = FontWeight.W400,
                    textAlign = TextAlign.Center
                )
            )
        },
        trailingIcon = {
            if (text.isNotEmpty()) Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.clickable {
                    onValueChange("")
                }
            )
        }, singleLine = true,
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = null, tint = DarkGray)
        }, modifier = Modifier
            .padding(0.dp)
            .shadow(elevation = 0.5.dp, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            selectionColors = TextSelectionColors(
                handleColor = Color.Blue,
                backgroundColor = Color.Blue
            ), focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            cursorColor = DarkGray,
            containerColor = LightGray
        ), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}