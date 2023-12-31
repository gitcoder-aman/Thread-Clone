package com.tech.threadclone.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tech.threadclone.screens.AddThreadScreen
import com.tech.threadclone.screens.BottomNav
import com.tech.threadclone.screens.HomeScreen
import com.tech.threadclone.screens.LoginScreen
import com.tech.threadclone.screens.NotificationScreen
import com.tech.threadclone.screens.OtherUserProfile
import com.tech.threadclone.screens.ProfileScreen
import com.tech.threadclone.screens.RegisterScreen
import com.tech.threadclone.screens.SearchScreen
import com.tech.threadclone.screens.SettingScreen
import com.tech.threadclone.screens.SplashScreen
import com.tech.threadclone.viewmodels.ProfileViewModel

@Composable
fun NavGraph(navController: NavHostController) {


    NavHost(navController = navController, startDestination = Routes.Splash.routes){

        composable(Routes.Splash.routes){
            SplashScreen(navController)
        }
        composable(Routes.BottomNav.routes){
            BottomNav(screenNavController = navController)
        }
        composable(Routes.Login.routes){
            LoginScreen(navController)
        }
        composable(Routes.Register.routes){
            RegisterScreen(navController)
        }
        composable(Routes.Setting.routes){
            SettingScreen(navController)
        }
        composable(Routes.OtherUserProfile.routes){
            val uid = it.arguments?.getString("uid")
            OtherUserProfile(navController,uid)
        }
    }
}