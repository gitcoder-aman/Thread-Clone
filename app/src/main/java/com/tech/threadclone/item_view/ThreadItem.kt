package com.tech.threadclone.item_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.tech.threadclone.R

@Composable
fun ThreadItem() {

    val context = LocalContext.current
    ConstraintLayout(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        val (userImage, userName, date, time, title, image, moreHor,divider) = createRefs()

        Image(painter = rememberAsyncImagePainter(
            model = R.drawable.test, placeholder = painterResource(
                id = R.drawable.test
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
            text = "UserName", style = TextStyle(
                fontSize = 16.sp
            ), modifier = Modifier.constrainAs(userName) {
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
            text = "Title", style = TextStyle(
                fontSize = 14.sp
            ), modifier = Modifier.constrainAs(title) {
                top.linkTo(userName.bottom)
                start.linkTo(userName.start)
                bottom.linkTo(userImage.bottom)
            }
        )
        Card(modifier = Modifier
            .padding()
            .constrainAs(image) {
                top.linkTo(title.bottom, margin = 8.dp)
                start.linkTo(title.start)
                end.linkTo(moreHor.end)
            }) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = R.drawable.test, placeholder = painterResource(
                        id = R.drawable.test
                    )
                ),
                contentDescription = "imageBox",
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxHeight()
                    .fillMaxWidth(0.8f),
                contentScale = ContentScale.Fit
            )
        }
        Divider(thickness = 0.2.dp, modifier = Modifier.constrainAs(divider){
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(image.bottom, margin = 12.dp)
        })
    }
}

@Preview(showBackground = true)
@Composable
fun ThreadItemScreen() {
    ThreadItem()
}