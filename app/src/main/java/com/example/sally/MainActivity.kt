package com.example.sally

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.sally.ui.components.SalyTopBar
import com.example.sally.ui.screens.chats.ChatsScreen
import com.example.sally.ui.screens.favorites.FavoritesScreen
import com.example.sally.ui.screens.home.HomeScreen
import com.example.sally.ui.screens.profile.ProfileScreen
import com.example.sally.ui.theme.PurpleStart
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp()
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val mainRoutes = listOf("home", "favorites", "chats", "profile")

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isMainScreen = currentRoute in mainRoutes

    val items = listOf(
        Triple("home", "Inicio", Icons.Default.Home),
        Triple("favorites", "Favoritos", Icons.Default.FavoriteBorder),
        Triple("chats", "Chats", Icons.Outlined.ChatBubbleOutline),
        Triple("profile", "Perfil", Icons.Outlined.Person)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = isMainScreen,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                NavigationDrawerItem(
                    label = { Text("Configuración") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (isMainScreen) {
                    SalyTopBar(onMenuClick = { scope.launch { drawerState.open() } })
                }
            },
            bottomBar = {
                if (isMainScreen) {
                    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                        items.forEach { (route, label, icon) ->
                            NavigationBarItem(
                                icon = { Icon(icon, contentDescription = label) },
                                label = { Text(label) },
                                selected = currentRoute == route,
                                onClick = {
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = PurpleStart,
                                    selectedTextColor = PurpleStart,
                                    indicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") { HomeScreen(navController) }
                composable("favorites") { FavoritesScreen(navController) }
                composable("chats") { ChatsScreen(navController) }
                composable("profile") { ProfileScreen(navController) }
            }
        }
    }
}