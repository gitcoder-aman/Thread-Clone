package com.tech.threadclone.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.tech.threadclone.R
import com.tech.threadclone.item_view.SuggestItem
import com.tech.threadclone.ui.theme.DarkGray
import com.tech.threadclone.ui.theme.LightGray
import com.tech.threadclone.utils.SharedRef
import com.tech.threadclone.utils.roboto_regular
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationScreen() {
    val navController = rememberNavController()

    val scrollState = rememberLazyListState()
    val tabTitles = listOf("Tab 1", "Tab 2", "Tab 3")

    // Remember the selected tab index
    var selectedTabIndex by remember { mutableStateOf(0) }

    val pagerState = rememberPagerState {
        tabItems.size
    }
//    LazyColumn(
//        state = scrollState,
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        item {
//            // Input field for searching
//            val lazyListState = rememberLazyListState()
//
//            val isScrollProgress =
//                remember { derivedStateOf { lazyListState.isScrollInProgress } }
//            val itemIndex =
//                remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
//            val canScrollBackward =
//                remember { derivedStateOf { lazyListState.canScrollBackward } }
//            val canScrollForward =
//                remember { derivedStateOf { lazyListState.canScrollForward } }
//
//            LazyRow(
//                state = lazyListState,
//                modifier = Modifier
//                    .padding(
//                        top = 16.dp,
//                        start = if ((isScrollProgress.value && canScrollBackward.value) || (itemIndex.value != 0)) 0.dp else 16.dp,
//                        end = if ((isScrollProgress.value && canScrollForward.value) || (itemIndex.value != 27)) 0.dp else 16.dp,
//                        bottom = 4.dp
//                    )
//            ) {
//                items(30) {
//                    SuggestItem(navHostController = navController)
//                }
//
//            }
//        }
//        stickyHeader {
//            // TabRow with tabs
//            TabRow(
//                selectedTabIndex = selectedTabIndex,
//            ) {
//                tabTitles.forEachIndexed { index, title ->
//                    Tab(
//                        text = { Text(title) },
//                        selected = selectedTabIndex == index,
//                        onClick = {
//                            selectedTabIndex = index
//                        }
//                    )
//                }
//            }
//        }
//        item {
//            // HorizontalPager with content for each tab
//            HorizontalPager(
//                state = pagerState
//            ) { page ->
//                when (page) {
//                    0 -> {
//                        // Content for Tab 1
//                        AddThreadScreen(navHostController = navController)
//                    }
//
//                    1 -> {
//                        // Content for Tab 2
//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .background(Color.Green)
//                        ) {
//                            Text("Content for Tab 2", color = Color.White)
//                        }
//                    }
//
//                    2 -> {
//                        // Content for Tab 3
//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .background(Color.Blue)
//                        ) {
//                            Text("Content for Tab 3", color = Color.White)
//                        }
//                    }
//                }
//            }
//        }
//    }

    val tabList = listOf("Posts", "Drafts", "Likes", "Favorites")
    val pagerState1: PagerState = rememberPagerState {
        tabList.size
    }
    val coroutineScope = rememberCoroutineScope()
    Surface(modifier = Modifier.fillMaxWidth()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                val lazyListState = rememberLazyListState()
                val isScrollProgress =
                    remember { derivedStateOf { lazyListState.isScrollInProgress } }
                val itemIndex =
                    remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
                val canScrollBackward =
                    remember { derivedStateOf { lazyListState.canScrollBackward } }
                val canScrollForward =
                    remember { derivedStateOf { lazyListState.canScrollForward } }

            }

            stickyHeader {
                TabRow(selectedTabIndex = selectedTabIndex, containerColor = Color.White) {
                    tabItems.forEachIndexed { index, item ->
                        Tab(selected = index == selectedTabIndex, onClick = {
                            selectedTabIndex = index
                        }, text = {
                            Text(
                                text = item, style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = roboto_regular,
                                    fontWeight = FontWeight.W400,
                                    color = Color.Black
                                )
                            )
                        })
                    }
                }
            }
            item {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                ) { index ->
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {

                        when (index) {
                            0 -> {
                                val navHostController = rememberNavController()
                                LazyColumn(Modifier.height(2500.dp)) {
                                    items(30) {
                                        Text(text = "Thread Post")
                                    }
                                }
                            }

                            1 -> {
                                ListFlowRow(items = 5)
                            }

                            2 -> {
                                ListLazyColum(items = 50)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListLazyColum(items: Int) {
    LazyColumn(Modifier.height(2500.dp)) {
        items(items) { index ->
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Button $index")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListFlowRow(items: Int) {
    FlowRow() {
        repeat(items) { index ->
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Button $index")
            }
        }

    }
}

