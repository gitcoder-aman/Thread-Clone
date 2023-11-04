package com.tech.threadclone.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tech.threadclone.models.BottomNavItem
import com.tech.threadclone.navigation.Routes
import com.tech.threadclone.utils.roboto_regular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNav(navController: NavHostController) {
    val navController1 = rememberNavController()

    Scaffold(
        bottomBar = { MyBottomBar(navController1) }
    ) { innerPadding ->
        NavHost(
            navController = navController1,
            startDestination = Routes.Home.routes,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.Notification.routes) {
                NotificationScreen()
            }
            composable(Routes.Home.routes) {
                HomeScreen(navController)
            }
            composable(Routes.Search.routes) {
                SearchScreen(navController)
            }
            composable(Routes.AddThread.routes) {
                AddThreadScreen(navController1)
            }
            composable(Routes.Profile.routes) {
                ProfileScreen(navController)
            }
        }
    }
}

@Composable
fun MyBottomBar(navController1: NavHostController) {

    val backStackEntry = navController1.currentBackStackEntryAsState()
    val list = listOf<BottomNavItem>(
        BottomNavItem(
            "Home",
            Routes.Home.routes,
            Icons.Rounded.Home
        ),
        BottomNavItem(
            "Search",
            Routes.Search.routes,
            Icons.Rounded.Search
        ),
        BottomNavItem(
            "Add Thread",
            Routes.AddThread.routes,
            Icons.Rounded.Add
        ),
        BottomNavItem(
            "Notification",
            Routes.Notification.routes,
            Icons.Rounded.Notifications
        ),
        BottomNavItem(
            "Profile",
            Routes.Profile.routes,
            Icons.Rounded.Person
        )
    )

    BottomAppBar {

        list.forEach {
            val selected = it.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController1.navigate(it.route) {
                        popUpTo(navController1.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(imageVector = it.icon, contentDescription = it.title)
                }, label = {
                    Text(text = it.title, style = TextStyle(
                        fontFamily = roboto_regular,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.W200,
                        color = Color.Black
                    ), maxLines = 1)
                }
            )
        }

    }
}
