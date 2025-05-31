package com.sourdoughtree.sourdoughtree

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sourdoughtree.sourdoughtree.auth.CognitoAuthManager
import com.sourdoughtree.sourdoughtree.screens.LoginScreenHost
import androidx.compose.runtime.getValue

object Route {
    const val screenLogin= "screenLogin"
    const val screenMain= "screenMain"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    var context = LocalContext.current
    val authManager = remember { CognitoAuthManager(context) }


    //Check if user is logged in - go to main screen if so
    authManager.checkSessionIfLoggedIn({  navController.navigate(Route.screenMain,{com.sourdoughtree.sourdoughtree.auth.CognitoAuthManager(context).logoutUser(SessionManager.username?: "")})})

    NavHost(navController = navController, startDestination = Route.screenLogin)
    {
        composable(route = Route.screenLogin) {
            LoginScreenHost(onLoginSuccess=
                {
                    navController.navigate(Route.screenMain, {
                    }
                    )
                }
            )
        }
        composable(route = Route.screenMain) {
            com.sourdoughtree.sourdoughtree.screens.MainScreenWithBottomNav(
                {
                    com.sourdoughtree.sourdoughtree.auth.CognitoAuthManager(context).logoutUser(SessionManager.username?:"")
                    navController.navigate(Route.screenLogin, {
                    }
                    )
                })
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Starter", "starter", R.drawable.jaricontrimmed),
        BottomNavItem("Bread", "bread", R.drawable.breadicontrimmed ),
        BottomNavItem("Recipes", "recipes", R.drawable.recipesicontrimmed )
    )

    //val currentRoute = navController.currentBackStackEntry?.destination?.route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White // or MaterialTheme.colorScheme.background
    ) {

        items.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconResId),
                        contentDescription = item.label,
                        modifier = Modifier.size(50.dp)  ,
                    tint = Color.Unspecified  // 👈 This disables the default monochrome tint
                    )
                },
                label = { Text(item.label) },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant // shows a rounded highlight behind selected item
                )
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val iconResId: Int
)

