package com.tech.threadclone.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.RoundaboutLeft
import androidx.compose.material.icons.rounded.RoundaboutLeft
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.tech.threadclone.navigation.Routes
import com.tech.threadclone.ui.theme.LightGray
import com.tech.threadclone.utils.roboto_bold
import com.tech.threadclone.utils.roboto_regular
import com.tech.threadclone.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavHostController
) {

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)
    val context = LocalContext.current

    val interactionSource = remember {
        MutableInteractionSource()
    }

    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null) {
            navController.navigate(Routes.Login.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Setting", style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    fontFamily = roboto_bold
                )
                )
            }, colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black,
                actionIconContentColor = Color.Black
            ), navigationIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "back",
                    modifier = Modifier.padding(start = 8.dp)
                        .clip(
                            CircleShape
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            navController.navigateUp()
                        }
                )
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            IconWithText(
                settingName = "Follow and invite friends",
                settingIcon = Icons.Outlined.PersonAdd
            ) {
                Toast.makeText(context, "Follow and invite friends", Toast.LENGTH_SHORT).show()
            }
            IconWithText(settingName = "Notification", settingIcon = Icons.Outlined.Notifications) {
                Toast.makeText(context, "Notification", Toast.LENGTH_SHORT).show()
            }
            IconWithText(settingName = "Your likes", settingIcon = Icons.Outlined.FavoriteBorder) {
                Toast.makeText(context, "Your likes", Toast.LENGTH_SHORT).show()
            }
            IconWithText(settingName = "Privacy", settingIcon = Icons.Outlined.PrivacyTip) {
                Toast.makeText(context, "Privacy", Toast.LENGTH_SHORT).show()
            }
            IconWithText(settingName = "Accessibility", settingIcon = Icons.Outlined.Accessibility) {
                Toast.makeText(context, "Accessibility", Toast.LENGTH_SHORT).show()
            }
            IconWithText(settingName = "Account", settingIcon = Icons.Outlined.AccountBox) {
                Toast.makeText(context, "Account", Toast.LENGTH_SHORT).show()
            }
            IconWithText(settingName = "Language", settingIcon = Icons.Outlined.Language) {
                Toast.makeText(context, "Language", Toast.LENGTH_SHORT).show()
            }
            IconWithText(settingName = "Help", settingIcon = Icons.Outlined.Help) {
                Toast.makeText(context, "Help", Toast.LENGTH_SHORT).show()
            }
            IconWithText(settingName = "About", settingIcon = Icons.Outlined.RoundaboutLeft) {
                Toast.makeText(context, "About", Toast.LENGTH_SHORT).show()
            }
            Divider(thickness = 0.5.dp, color = LightGray)

            TextButton(onClick = {
                authViewModel.logout()
            },modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = "Log out", style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.W400,
                        fontFamily = roboto_regular
                    )
                )
            }
        }
    }
}

@Composable
fun IconWithText(
    settingName: String,
    settingIcon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .fillMaxWidth()
            .padding(12.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = settingIcon,
            contentDescription = null,
            modifier = Modifier
                .clip(
                    CircleShape
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = settingName, style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.W400,
                fontFamily = roboto_regular
            )
        )
    }
}