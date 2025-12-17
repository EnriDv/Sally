package com.example.sally

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.sally.data.models.ClosedState
import com.example.sally.data.models.OpenState
import com.example.sally.data.models.mockSalons
import com.example.sally.data.SupabaseClient
import com.example.sally.ui.components.SalyTopBar
import com.example.sally.ui.screens.auth.LoginScreen
import com.example.sally.ui.screens.auth.RegisterScreen
import com.example.sally.ui.screens.chats.ChatsScreen
import com.example.sally.ui.screens.favorites.FavoritesScreen
import com.example.sally.ui.screens.home.HomeScreen
import com.example.sally.ui.screens.map.MapScreen
import com.example.sally.ui.screens.profile.*
import com.example.sally.ui.screens.salon.AppointmentsScreen
import com.example.sally.ui.screens.salon.BookingScreen
import com.example.sally.ui.screens.salon.SalonProfileScreen
import com.example.sally.ui.theme.SallyTheme
import com.example.sally.utils.AppThemeMode
import com.example.sally.utils.ThemeManager
import com.google.android.gms.maps.model.LatLng
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeManager.init(applicationContext)

        setContent {
            val themeMode by ThemeManager.themeMode.collectAsState()

            val isDark = when (themeMode) {
                AppThemeMode.DARK -> true
                AppThemeMode.LIGHT -> false
                AppThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            SallyTheme(darkTheme = isDark) {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // En MainApp()
    val startDestination = try {
        if (SupabaseClient.client.auth.currentUserOrNull() != null) "home" else "login"
    } catch (e: Exception) {
        e.printStackTrace() // Mira el error en el Logcat
        "login" // Valor por defecto para que no crashee
    }

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
                    onClick = { scope.launch { drawerState.close() } },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
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
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp
                    ) {
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
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
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
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("login") { LoginScreen(navController) }
                composable("register") { RegisterScreen(navController) }
                composable("home") { HomeScreen(navController) }
                composable("favorites") { FavoritesScreen(navController) }
                composable("chats") { ChatsScreen(navController) }
                composable("profile") { ProfileScreen(navController) }

                composable(
                    route = "salon_profile/{salonId}",
                    arguments = listOf(navArgument("salonId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val salonId = backStackEntry.arguments?.getLong("salonId") ?: 0L

                    val selectedSalon = mockSalons.find { it.id == salonId } ?: mockSalons[0]
                    val initialState = if (selectedSalon.isClosed) ClosedState() else OpenState()

                    SalonProfileScreen(
                        navController = navController,
                        initialState = initialState,
                        salonData = selectedSalon
                    )
                }
                composable(
                    route = "appointments/{tabIndex}",
                    arguments = listOf(navArgument("tabIndex") { type = NavType.IntType })
                ) { backStackEntry ->
                    val tabIndex = backStackEntry.arguments?.getInt("tabIndex") ?: 0
                    AppointmentsScreen(navController = navController, initialTab = tabIndex)
                }
                composable(
                    // AGREGAMOS {salonId} A LA RUTA
                    route = "booking/{salonId}/{salonName}/{salonAddress}/{serviceName}/{servicePrice}",
                    arguments = listOf(
                        // AGREGAMOS EL ARGUMENTO LONG
                        navArgument("salonId") { type = NavType.LongType },
                        navArgument("salonName") { type = NavType.StringType },
                        navArgument("salonAddress") { type = NavType.StringType },
                        navArgument("serviceName") { type = NavType.StringType },
                        navArgument("servicePrice") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val salonId = backStackEntry.arguments?.getLong("salonId") ?: 0L
                    val salonName = backStackEntry.arguments?.getString("salonName") ?: ""
                    val salonAddress = backStackEntry.arguments?.getString("salonAddress") ?: ""
                    val serviceName = backStackEntry.arguments?.getString("serviceName") ?: ""
                    val servicePrice = backStackEntry.arguments?.getString("servicePrice") ?: ""

                    BookingScreen(
                        navController = navController,
                        salonId = salonId,
                        salonName = salonName,
                        salonAddress = salonAddress,
                        serviceName = serviceName,
                        servicePrice = servicePrice
                    )
                }
                composable(
                    route = "map/{salonName}/{lat}/{lng}",
                    arguments = listOf(
                        navArgument("salonName") { type = NavType.StringType },
                        navArgument("lat") { type = NavType.FloatType },
                        navArgument("lng") { type = NavType.FloatType }
                    )
                ) { backStackEntry ->
                    val name = backStackEntry.arguments?.getString("salonName") ?: ""
                    val lat = backStackEntry.arguments?.getFloat("lat") ?: 0f
                    val lng = backStackEntry.arguments?.getFloat("lng") ?: 0f

                    MapScreen(
                        navController = navController,
                        salonName = name,
                        targetLocation = LatLng(lat.toDouble(), lng.toDouble())
                    )
                }
                composable("profile_personal_info") { PersonalInfoScreen(navController) }
                composable("profile_security") { SecurityScreen(navController) }
                composable("profile_language") { LanguageScreen(navController) }
                composable("profile_privacy") { PrivacyScreen(navController) }
                composable("profile_help") { HelpCenterScreen(navController) }
                composable("profile_support") { SupportScreen(navController) }
                composable("profile_theme") { ThemeSelectionScreen(navController) }
            }
        }
    }
}