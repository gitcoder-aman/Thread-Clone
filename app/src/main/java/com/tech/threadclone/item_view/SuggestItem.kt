package com.tech.threadclone.item_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.tech.threadclone.R
import com.tech.threadclone.models.UserModel
import com.tech.threadclone.navigation.Routes
import com.tech.threadclone.ui.theme.DarkGray
import com.tech.threadclone.ui.theme.LightGray
import com.tech.threadclone.utils.roboto_regular
import com.tech.threadclone.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestItem(
    userModel: UserModel,
    navHostController: NavHostController,
    profileViewModel: ProfileViewModel
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isFollowing by rememberSaveable { mutableStateOf(false) }
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


    Card(
        onClick = {
            val routes = Routes.OtherUserProfile.routes.replace("{uid}", userModel.uid!!)
            navHostController.navigate(routes)
        },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp, 2.dp),
        colors = CardDefaults.cardColors(containerColor = LightGray, contentColor = Color.Black),
        modifier = Modifier
            .size(155.dp, 200.dp)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close, contentDescription = "Remove Suggested Profile.",
                modifier = Modifier
                    .size(14.dp)
                    .align(Alignment.TopEnd)
                    .clickable(interactionSource = interactionSource, indication = null) {

                    }, tint = DarkGray
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = userModel.imageUrl,
                        placeholder = painterResource(id = R.drawable.man),
                        error = painterResource(
                            id = R.drawable.man
                        )
                    ),
                    contentDescription = "person",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(
                            CircleShape
                        )
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = userModel.name,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = userModel.userName,
                    style = TextStyle(
                        fontFamily = roboto_regular,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W300,
                        color = DarkGray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomFollowButton(isFollowing){
                    if(!isFollowing) {
                        profileViewModel.followUsers(userModel.uid!!, currentUserId)
                    }else{
                        profileViewModel.unFollowUser(userModel.uid!!,currentUserId)
                        isFollowing = false
                    }
                }
            }
        }
    }
}

@Composable
fun CustomFollowButton(isFollowing: Boolean, onClick:()->Unit) {
    Box(
        modifier = Modifier
            .padding(start = 4.dp)
            .size(150.dp, 30.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                0.5.dp,
                Color.Gray,
                RoundedCornerShape(8.dp)
            )
            .background(if (isFollowing) Color.Transparent else Color.Black)
            .clickable {
//                isFollowing = !isFollowing
                onClick()
            }, contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isFollowing) "Following" else "Follow",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                color = if (isFollowing) DarkGray else Color.White
            )
        )
    }
}