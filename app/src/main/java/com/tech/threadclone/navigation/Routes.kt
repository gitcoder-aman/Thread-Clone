package com.tech.threadclone.navigation

sealed class Routes(val routes: String){
    object Home : Routes("home")
    object Notification : Routes("notification")
    object Profile : Routes("profile")
    object Search : Routes("search")
    object Splash : Routes("splash")
    object AddThread : Routes("add_thread")
    object BottomNav : Routes("bottom_nav")
    object Login : Routes("login")
    object Register : Routes("register")
    object Setting : Routes("setting")
    object OtherUserProfile : Routes("other_user_profile/{uid}")
}
