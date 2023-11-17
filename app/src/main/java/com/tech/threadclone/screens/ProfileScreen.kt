package com.tech.threadclone.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.tech.threadclone.R
import com.tech.threadclone.item_view.SuggestItem
import com.tech.threadclone.item_view.ThreadItem
import com.tech.threadclone.models.UserModel
import com.tech.threadclone.navigation.Routes
import com.tech.threadclone.ui.theme.DarkGray
import com.tech.threadclone.ui.theme.LightGray
import com.tech.threadclone.utils.SharedRef
import com.tech.threadclone.utils.roboto_regular
import com.tech.threadclone.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController
) {

    val profileViewModel : ProfileViewModel = viewModel()
    val threads by profileViewModel.threads.observeAsState(null)
    val allUsers by profileViewModel.allUsers.observeAsState(null)
    val followersList by profileViewModel.followersList.observeAsState(null)
    val followingList by profileViewModel.followingList.observeAsState(null)

    var currentUserId = ""
    if (FirebaseAuth.getInstance().currentUser?.uid != null)
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid!!

    if(currentUserId != "") {
        profileViewModel.getFollowers(currentUserId)
        profileViewModel.getFollowing(currentUserId)
    }
    val context = LocalContext.current

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val userModel = UserModel(
        name = SharedRef.getName(context),
        bio = SharedRef.getBio(context),
        imageUrl = SharedRef.getImageUrl(context),
        userName = SharedRef.getUserName(context),
        uid = FirebaseAuth.getInstance().currentUser!!.uid
    )

    profileViewModel.fetchThreads(FirebaseAuth.getInstance().currentUser!!.uid)


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            item {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    val ( privacyBtn, settingBtn, instaBtn,bio, userLogo, userName,
                        name, totalFollowers,totalFollowing, editBtn, shareBtn, suggestText, lazyRow, divider, tabRow) = createRefs()

                    Icon(
                        painter = painterResource(id = R.drawable.internet),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp)
                            .size(24.dp)
                            .clip(CircleShape)
                            .constrainAs(privacyBtn) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }, tint = Color.Black
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.drawer_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                navController.navigate(Routes.Setting.routes)
                            }
                            .padding(end = 16.dp, top = 16.dp)
                            .size(24.dp)
                            .clip(CircleShape)
                            .constrainAs(settingBtn) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                            }, tint = Color.Black
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.instagram_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .size(24.dp)
                            .clip(CircleShape)
                            .constrainAs(instaBtn) {
                                top.linkTo(parent.top)
                                end.linkTo(settingBtn.start, margin = 14.dp)
                            }, tint = Color.Black
                    )

                    Text(
                        text = SharedRef.getName(context), style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W600
                        ), modifier = Modifier
                            .padding(end = 24.dp, start = 16.dp)
                            .constrainAs(name) {
                                top.linkTo(privacyBtn.bottom, margin = 24.dp)
                                start.linkTo(parent.start)
                            }
                    )
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = SharedRef.getImageUrl(context),
                            placeholder = painterResource(id = R.drawable.man),
                            error = painterResource(
                                id = R.drawable.man
                            )
                        ),
                        contentDescription = "person",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .constrainAs(userLogo) {
                                end.linkTo(parent.end)
                                top.linkTo(name.top, margin = 12.dp)
                                bottom.linkTo(name.bottom)
                            }
                            .size(70.dp)
                            .clip(
                                CircleShape
                            )
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = SharedRef.getUserName(context), style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600
                        ), modifier = Modifier
                            .padding(start = 16.dp)
                            .constrainAs(userName) {
                                top.linkTo(name.bottom, margin = 16.dp)
                                start.linkTo(parent.start)
                            }
                    )
                    Text(
                        text = SharedRef.getBio(context), style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W300,
                            fontFamily = roboto_regular
                        ), modifier = Modifier
                            .padding(start = 16.dp, end = 24.dp)
                            .constrainAs(bio) {
                                top.linkTo(userName.bottom, margin = 14.dp)
                                start.linkTo(parent.start)
                            }
                    )
                    Text(
                        text = "${followersList?.size} followers", style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W300,
                            fontFamily = roboto_regular,
                            color = DarkGray
                        ), modifier = Modifier
                            .padding(start = 16.dp)
                            .constrainAs(totalFollowers) {
                                top.linkTo(bio.bottom, margin = 12.dp)
                                start.linkTo(parent.start)
                            }
                    )
                    Text(
                        text = "${followingList?.size} following", style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W300,
                            fontFamily = roboto_regular,
                            color = DarkGray
                        ), modifier = Modifier
                            .padding(start = 16.dp)
                            .constrainAs(totalFollowing) {
                                start.linkTo(totalFollowers.end, margin = 8.dp)
                                top.linkTo(totalFollowers.top)
                                bottom.linkTo(totalFollowers.bottom)
                            }
                    )
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 4.dp)
                            .constrainAs(editBtn) {
                                top.linkTo(totalFollowers.bottom, margin = 20.dp)
                                start.linkTo(parent.start)
                                end.linkTo(shareBtn.start)
                                width = Dimension.fillToConstraints
                            }
                            .size(150.dp, 30.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                0.5.dp,
                                Color.Gray,
                                RoundedCornerShape(8.dp)
                            )
                            .background(Color.White)
                            .clickable {

                            }, contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Edit Profile",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W600,
                                color = Color.Black
                            ),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 16.dp)
                            .constrainAs(shareBtn) {
                                top.linkTo(editBtn.top)
                                end.linkTo(parent.end)
                                start.linkTo(editBtn.end)
                                bottom.linkTo(editBtn.bottom)
                                width = Dimension.fillToConstraints
                            }
                            .size(150.dp, 30.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                0.5.dp,
                                Color.Gray,
                                RoundedCornerShape(8.dp)
                            )
                            .background(Color.White)
                            .clickable {

                            }, contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Share Profile",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W600,
                                color = Color.Black
                            ),
                        )
                    }
                    Text(
                        text = "Suggested for you",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600,
                            color = Color.Black
                        ), modifier = Modifier
                            .padding(top = 20.dp, start = 16.dp)
                            .constrainAs(suggestText) {
                                top.linkTo(editBtn.bottom)
                                start.linkTo(parent.start)
                            }
                    )

                    val lazyListState = rememberLazyListState()

                    val isScrollProgress =
                        remember { derivedStateOf { lazyListState.isScrollInProgress } }
                    val itemIndex =
                        remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
                    val canScrollBackward =
                        remember { derivedStateOf { lazyListState.canScrollBackward } }
                    val canScrollForward =
                        remember { derivedStateOf { lazyListState.canScrollForward } }

                    LazyRow(state = lazyListState,
                        modifier = Modifier
                            .padding(
                                top = 16.dp,
                                start = if ((isScrollProgress.value && canScrollBackward.value) || (itemIndex.value != 0)) 0.dp else 16.dp,
                                end = if ((isScrollProgress.value && canScrollForward.value) || (itemIndex.value != allUsers?.size?.minus(2))) 0.dp else 16.dp,
                                bottom = 4.dp
                            )
                            .constrainAs(lazyRow) {
                                top.linkTo(suggestText.bottom)
                                start.linkTo(parent.start)
                            }) {
                        items(allUsers ?: emptyList()) {user->
                            if(user.uid != currentUserId)
                            SuggestItem(userModel = user,navHostController = navController,profileViewModel)
                        }

                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(
                        thickness = 0.5.dp,
                        color = LightGray,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .constrainAs(divider) {
                                top.linkTo(lazyRow.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            })
//                TabRowCreate(
//                    modifier = Modifier
//                        .constrainAs(tabRow) {
//                            top.linkTo(divider.bottom)
//                            start.linkTo(parent.start)
//                            end.linkTo(parent.end)
//                        }
//                )
                }
            }

            items(threads ?: emptyList()) { thread ->
                ThreadItem(
                    threadModel = thread,
                    userModel = userModel,
                    screenNavController = navController
                )
            }
        }
}

val tabItems = listOf("Threads", "Replies", "Reposts")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabRowCreate(
    modifier: Modifier
) {

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState {
        tabItems.size
    }
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress)
            selectedTabIndex = pagerState.currentPage
    }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
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
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) { index ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {

                if (index == 0) {
                    val navHostController = rememberNavController()
                    AddThreadScreen(navHostController = navHostController)
                }
                Text(text = tabItems[index])

                val context = LocalContext.current

                Image(
                    painter = rememberAsyncImagePainter(
                        model = SharedRef.getImageUrl(context),
                        placeholder = painterResource(id = R.drawable.man),
                        error = painterResource(
                            id = R.drawable.man
                        )
                    ),
                    contentDescription = "person",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(70.dp)
                        .clip(
                            CircleShape
                        )
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )

            }
        }
    }
}