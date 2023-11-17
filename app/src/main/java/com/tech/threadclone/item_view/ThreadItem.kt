package com.tech.threadclone.item_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RepeatOne
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.tech.threadclone.utils.roboto_regular

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
        val (userImage, userName, time, title, image, moreHor, likeBtn, commentBtn,
            repostBtn, shareBtn, totalLikes, totalReplies) = createRefs()

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
        Text(text = "21 h", style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.W300,
            color = Color.Gray,
            fontFamily = roboto_regular
        ), modifier = Modifier.constrainAs(time) {
            end.linkTo(moreHor.start, margin = 8.dp)
            top.linkTo(moreHor.top)
            bottom.linkTo(moreHor.bottom)
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
        var isLiked by rememberSaveable {
            mutableStateOf(false)
        }
        Icon(
            painter = painterResource(id = if(!isLiked) R.drawable.heart else R.drawable.filled_heart),
            tint = Color.Unspecified,
            contentDescription = null,
            modifier = Modifier
                .size(25.dp)
                .constrainAs(likeBtn) {
                    start.linkTo(if (threadModel.imageUrl != "") image.start else title.start)
                    top.linkTo(
                        if (threadModel.imageUrl != "") image.bottom else title.bottom,
                        margin = 16.dp
                    )
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    isLiked = !isLiked
                })
        Icon(
            painter = painterResource(id = R.drawable.comment),
            tint = Color.Unspecified,
            contentDescription = null,
            modifier = Modifier
                .size(25.dp)
                .constrainAs(commentBtn) {
                    start.linkTo(likeBtn.end, margin = 12.dp)
                    top.linkTo(likeBtn.top)
                    bottom.linkTo(likeBtn.bottom)
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {

                })
        Icon(
            painter = painterResource(id = R.drawable.noun_repost_2508427),
            tint = Color.Unspecified,
            contentDescription = null,
            modifier = Modifier
                .size(25.dp)
                .constrainAs(repostBtn) {
                    start.linkTo(commentBtn.end, margin = 12.dp)
                    top.linkTo(commentBtn.top)
                    bottom.linkTo(commentBtn.bottom)
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {

                })
        Icon(
            painter = painterResource(id = R.drawable.icons8_share),
            tint = Color.Unspecified,
            contentDescription = null,
            modifier = Modifier
                .size(25.dp)
                .constrainAs(shareBtn) {
                    start.linkTo(repostBtn.end, margin = 12.dp)
                    top.linkTo(repostBtn.top)
                    bottom.linkTo(repostBtn.bottom)
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {

                })
        Text(text = "25 replies.", style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.W300,
            color = Color.Gray,
            fontFamily = roboto_regular
        ), modifier = Modifier.constrainAs(totalReplies) {
            start.linkTo(likeBtn.start)
            top.linkTo(likeBtn.bottom, margin = 16.dp)
        }
        )
        Text(text = "1145 likes", style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.W300,
            color = Color.Gray,
            fontFamily = roboto_regular
        ), modifier = Modifier.constrainAs(totalLikes) {
            start.linkTo(totalReplies.end, margin = 4.dp)
            top.linkTo(totalReplies.top)
            bottom.linkTo(totalReplies.bottom)
        }
        )

    }
    Divider(thickness = 0.2.dp, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
}