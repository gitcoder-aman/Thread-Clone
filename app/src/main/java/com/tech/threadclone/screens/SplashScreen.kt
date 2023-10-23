package com.tech.threadclone.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.tech.threadclone.R
import com.tech.threadclone.navigation.Routes
import com.tech.threadclone.utils.roboto_bold
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        val (image) = createRefs()
        val (appName) = createRefs()
        Image(painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .size(48.dp))

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Thread", style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.W600,
            fontFamily = roboto_bold,
            color = Color.Black
        ), modifier = Modifier.constrainAs(appName) {
            top.linkTo(image.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

    }

    LaunchedEffect(true) {
        delay(3000)

        if (FirebaseAuth.getInstance().currentUser != null) {
            navController.navigate(Routes.BottomNav.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }else{
            navController.navigate(Routes.Login.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

}