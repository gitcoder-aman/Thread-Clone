package com.tech.threadclone.item_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.google.firebase.auth.FirebaseAuth
import com.tech.threadclone.R
import com.tech.threadclone.models.ThreadModel
import com.tech.threadclone.models.UserModel
import com.tech.threadclone.navigation.Routes

@Composable
fun ThreadItem(
    threadModel: ThreadModel,
    userModel: UserModel,
    screenNavController: NavHostController,
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }
    ConstraintLayout(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(bottom = 12.dp)
    ) {
        val (userImage, userName, date, time, title, image, moreHor) = createRefs()

        Image(painter = rememberAsyncImagePainter(
            model = userModel.imageUrl, placeholder = painterResource(
                id = R.drawable.logo
            )
        ), contentDescription = "image",
            modifier = Modifier
                .padding(start = 16.dp)
                .constrainAs(userImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .size(36.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            text = userModel.userName, style = TextStyle(
                fontSize = 16.sp
            ), modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    if (userModel.uid != FirebaseAuth.getInstance().currentUser!!.uid) {
                        val routes =
                            Routes.OtherUserProfile.routes.replace("{uid}", userModel.uid!!)
                        screenNavController.navigate(routes)
                    }
                }
                .constrainAs(userName) {
                    top.linkTo(userImage.top)
                    start.linkTo(userImage.end, margin = 10.dp)
                }
        )
        Icon(painter = painterResource(id = R.drawable.baseline_more_horiz_24),
            contentDescription = null,
            modifier = Modifier.constrainAs(moreHor) {
                end.linkTo(parent.end, margin = 8.dp)
                top.linkTo(parent.top)
            })
        Text(
            text = threadModel.threadText, style = TextStyle(
                fontSize = 14.sp,
            ), modifier = Modifier
                .padding(end = 70.dp)
                .constrainAs(title) {
                    top.linkTo(userName.bottom, margin = 2.dp)
                    start.linkTo(userName.start)
                }
        )
        if (threadModel.imageUrl != "") {
            Card(modifier = Modifier
                .padding(end = 70.dp)
                .constrainAs(image) {
                    top.linkTo(title.bottom, margin = 8.dp)
                    start.linkTo(title.start)
                }) {
                val model = ImageRequest.Builder(LocalContext.current)
                    .data(threadModel.imageUrl)
                    .size(Size.ORIGINAL)
                    .placeholder(R.drawable.placeholder_image)
                    .crossfade(true)
                    .build()
                val painter = rememberAsyncImagePainter(model)
                Image(
                    painter = painter,
                    contentDescription = "imageBox",
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
            }
        }

    }
    Divider(thickness = 0.2.dp, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
}