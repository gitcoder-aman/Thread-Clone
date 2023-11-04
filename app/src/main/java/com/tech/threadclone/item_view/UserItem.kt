package com.tech.threadclone.item_view

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.tech.threadclone.R
import com.tech.threadclone.models.UserModel

@Composable
fun UserItem(
    userModel: UserModel,
    navHostController: NavHostController
) {
    var isFollowing by remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(start = 16.dp,bottom = 16.dp,end = 16.dp)
    ) {
        val (userImg, userName, name, followBtn, divider) = createRefs()
        Image(
            painter = rememberAsyncImagePainter(
                model = userModel.imageUrl, placeholder = painterResource(
                    id = R.drawable.logo
                )
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
                .border(0.5.dp, if(isFollowing) Color.LightGray else Color.Gray, RoundedCornerShape(8.dp))
                .background(Color.White)
                .clickable {
                    isFollowing = !isFollowing
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