package com.tech.threadclone.item_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.tech.threadclone.R
import com.tech.threadclone.models.UserModel
import com.tech.threadclone.navigation.Routes
import com.tech.threadclone.viewmodels.ProfileViewModel

@Composable
fun UserItem(
    userModel: UserModel,
    screenNavController: NavHostController,
) {
    var isFollowing by rememberSaveable { mutableStateOf(false) }

    val profileViewModel : ProfileViewModel = viewModel()
    val suggestFollowersList by profileViewModel.followingList.observeAsState(null)


    var currentUserId = ""
    if (FirebaseAuth.getInstance().currentUser?.uid != null)
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid!!

    if(currentUserId != ""){
        profileViewModel.getFollowing(currentUserId)
    }
    if (!suggestFollowersList.isNullOrEmpty() && suggestFollowersList!!.contains(userModel.uid)){
        isFollowing = true
    }
    ConstraintLayout(
        modifier = Modifier.clickable {
            if(userModel.uid != FirebaseAuth.getInstance().uid) {
                val routes =
                    Routes.OtherUserProfile.routes.replace("{uid}", userModel.uid!!)
                screenNavController.navigate(routes)
            }
        }
            .background(Color.White)
            .padding(top = 12.dp)
            .fillMaxSize()
    ) {
        val (userImg, userName, name, followBtn, divider) = createRefs()
        Image(
            painter = rememberAsyncImagePainter(
                model = userModel.imageUrl, placeholder = painterResource(
                    id = R.drawable.man
                ),error = painterResource(id = R.drawable.man)
            ),
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .constrainAs(userImg) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }, contentScale = ContentScale.Crop
        )
        Text(text = userModel.userName, style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
            color = Color.Black
        ), modifier = Modifier.constrainAs(userName) {
            start.linkTo(userImg.end, margin = 12.dp)
            top.linkTo(userImg.top)
        })
        Text(text = userModel.name, style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.W300,
            color = Color.Gray
        ), modifier = Modifier.constrainAs(name) {
            top.linkTo(userName.bottom)
            start.linkTo(userName.start)
            bottom.linkTo(userImg.bottom)
        })
        Box(
            modifier = Modifier
                .constrainAs(followBtn) {
                    end.linkTo(parent.end)
                    top.linkTo(userName.top)
                    bottom.linkTo(name.bottom)
                }
                .size(90.dp, 30.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    0.5.dp,
                    if (isFollowing) Color.LightGray else Color.Gray,
                    RoundedCornerShape(8.dp)
                )
                .background(Color.White)
                .clickable {
                    if(!isFollowing) {
                        profileViewModel.followUsers(userModel.uid!!, currentUserId)
                    }else{
                        profileViewModel.unFollowUser(userModel.uid!!,currentUserId)
                        isFollowing = false
                    }
                }, contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isFollowing) "Following" else "Follow",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                    color = if(isFollowing) Color.LightGray else Color.Black
                ),
            )
        }
        Divider(thickness = 0.2.dp, color = Color.Gray, modifier = Modifier.constrainAs(divider) {
            start.linkTo(name.start)
            top.linkTo(name.bottom, margin = 12.dp)
        })
    }
}