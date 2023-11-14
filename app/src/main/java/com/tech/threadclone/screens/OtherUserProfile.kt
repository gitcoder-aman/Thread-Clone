package com.tech.threadclone.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.IosShare
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
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
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.tech.threadclone.R
import com.tech.threadclone.item_view.SuggestItem
import com.tech.threadclone.item_view.ThreadItem
import com.tech.threadclone.ui.theme.DarkGray
import com.tech.threadclone.ui.theme.LightGray
import com.tech.threadclone.utils.roboto_bold
import com.tech.threadclone.utils.roboto_regular
import com.tech.threadclone.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherUserProfile(navController: NavHostController, uid: String?) {

    val profileViewModel: ProfileViewModel = viewModel()
    val threads by profileViewModel.threads.observeAsState(null)
    val userModel by profileViewModel.users.observeAsState(null)
    val allUsers by profileViewModel.allUsers.observeAsState(null)
    val context = LocalContext.current

    profileViewModel.fetchThreads(uid!!)
    profileViewModel.fetchUser(uid)

    val interactionSource = remember {
        MutableInteractionSource()
    }
    var isFollowing by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen = rememberSaveable {
        mutableStateOf(false)
    }

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
                val (privacyBtn, settingBtn, instaBtn, bio, userLogo, userName,
                    name, totalFollowers, editBtn, shareBtn, suggestText, lazyRow, divider, tabRow) = createRefs()

                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            navController.navigateUp()
                        }
                        .padding(start = 16.dp, top = 16.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .constrainAs(privacyBtn) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }, tint = Color.Black
                )

                Icon(
                    imageVector = Icons.Outlined.MoreHoriz,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            isSheetOpen.value = true
                        }
                        .padding(end = 16.dp, top = 16.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .constrainAs(settingBtn) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }, tint = Color.Black
                )
                if (isSheetOpen.value) {
                    BottomSheet(sheetState, isSheetOpen)
                }
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
                    text = userModel?.name!!, style = TextStyle(
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
                        model = userModel?.imageUrl,
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
                    text = userModel?.userName!!, style = TextStyle(
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
                    text = userModel?.bio!!, style = TextStyle(
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
                    text = "28 followers", style = TextStyle(
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
                        .background(if (isFollowing) Color.Transparent else Color.Black)
                        .clickable {
                            isFollowing = !isFollowing
                        }, contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Follow",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600,
                            color = if (isFollowing) DarkGray else Color.White
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
                        text = "Mention",
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
                            end = if ((isScrollProgress.value && canScrollForward.value) || (itemIndex.value != 27)) 0.dp else 16.dp,
                            bottom = 4.dp
                        )
                        .constrainAs(lazyRow) {
                            top.linkTo(suggestText.bottom)
                            start.linkTo(parent.start)
                        }) {
                    items(allUsers ?: emptyList()) { user ->

                        if (user.uid != userModel?.uid && user.uid != FirebaseAuth.getInstance().uid)
                            SuggestItem(userModel = user, navHostController = navController)
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
            }
        }

        if (threads != null && userModel != null) {

            items(threads ?: emptyList()) { thread ->
                ThreadItem(
                    threadModel = thread,
                    userModel = userModel!!,
                    screenNavController = navController
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(sheetState: SheetState, isSheetOpen: MutableState<Boolean>) {

    val context = LocalContext.current
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            isSheetOpen.value = false
        }, containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .align(Start)
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = CenterVertically
            ) {
                TextButton(
                    onClick = {
                        Toast.makeText(context, "Copy Link", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightGray,
                        contentColor = Color.Black
                    ), modifier = Modifier
                        .align(CenterVertically)
                        .size(150.dp, 80.dp)
                        .weight(0.5f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Link,
                            contentDescription = "copy link",
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Copy Link")
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    onClick = {
                        Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightGray,
                        contentColor = Color.Black
                    ), modifier = Modifier
                        .align(CenterVertically)
                        .size(150.dp, 80.dp)
                        .weight(0.5f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.IosShare,
                            contentDescription = "share",
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Share via...")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = {

                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightGray,
                    contentColor = Color.Black
                ), modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp), contentAlignment = CenterStart
                ) {
                    Text(
                        text = "About this Profile", style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W300,
                            fontFamily = roboto_bold,
                            color = Color.Black
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            TwoButtonMerge(
                firstBtnName = "Mute",
                secondButtonName = "Restrict",
                textColor = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            TwoButtonMerge(
                firstBtnName = "Block",
                secondButtonName = "Report",
                textColor = Color.Red
            )

        }
    }
}

@Composable
fun TwoButtonMerge(firstBtnName: String, secondButtonName: String, textColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White), contentAlignment = CenterStart
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Start,
            verticalArrangement = Arrangement.Center
        ) {
            TextButton(
                onClick = {

                },
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightGray,
                    contentColor = Color.Black
                ), modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp), contentAlignment = CenterStart
                ) {
                    Text(
                        text = firstBtnName, style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W300,
                            fontFamily = roboto_bold,
                            color = textColor
                        )
                    )
                }
            }
            Divider(thickness = 0.5.dp, color = DarkGray)
            TextButton(
                onClick = {

                },
                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightGray,
                    contentColor = Color.Black
                ), modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp), contentAlignment = CenterStart
                ) {
                    Text(
                        text = secondButtonName, style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W300,
                            fontFamily = roboto_bold,
                            color = textColor
                        )
                    )
                }
            }
        }
    }
}