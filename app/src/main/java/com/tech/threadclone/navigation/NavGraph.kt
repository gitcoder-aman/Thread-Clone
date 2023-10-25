package com.tech.threadclone.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tech.threadclone.screens.AddThreadScreen
import com.tech.threadclone.screens.BottomNav
import com.tech.threadclone.screens.HomeScreen
import com.tech.threadclone.screens.LoginScreen
import com.tech.threadclone.screens.NotificationScreen
import com.tech.threadclone.screens.ProfileScreen
import com.tech.threadclone.screens.RegisterScreen
import com.tech.threadclone.screens.SearchScreen
import com.tech.threadclone.screens.SplashScreen

@Composable
fun NavGraph(navController : NavHostController) {

    NavHost(navController = navController, startDestination = Routes.Splash.routes){

        composable(Routes.Splash.routes){
            SplashScreen(navController)
        }
        composable(Routes.Home.routes){
            HomeScreen()
        }
        composable(Routes.Search.routes){
            SearchScreen()
        }
        composable(Routes.AddThread.routes){
            AddThreadScreen()
        }
        composable(Routes.Profile.routes){
            ProfileScreen(navController = navController)
        }
        composable(Routes.Notification.routes){
            NotificationScreen()
        }
        composable(Routes.BottomNav.routes){
            BottomNav(navController = navController)
        }
        composable(Routes.Login.routes){
            LoginScreen(navController)
        }
        composable(Routes.Register.routes){
            RegisterScreen(navController)
        }
    }
}