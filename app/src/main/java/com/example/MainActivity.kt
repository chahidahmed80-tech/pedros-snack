package com.example

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.data.repository.PedroRepository
import com.example.ui.components.CartManager
import com.example.ui.components.LocalCartManager
import com.example.ui.components.NavDestination
import com.example.ui.components.PedrosBottomBar
import com.example.ui.components.PedrosTopBar
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MenuScreen
import com.example.ui.screens.OffersScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.theme.PedrosSnackTheme
import com.example.ui.theme.ThemeController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = PedroRepository(this)

        setContent {
            val themeController = remember { ThemeController() }
            var currentLanguage by remember { mutableStateOf(AppLanguage.FRENCH) }
            val cartManager = remember { CartManager() }

            CompositionLocalProvider(
                LocalAppLanguage provides currentLanguage,
                LocalLayoutDirection provides currentLanguage.layoutDirection,
                LocalCartManager provides cartManager
            ) {
                PedrosSnackTheme(controller = themeController) {
                    PedrosApp(
                        repository = repository,
                        onLanguageSelected = { currentLanguage = it }
                    )
                }
            }
        }
    }
}

@Composable
fun PedrosApp(
    repository: PedroRepository,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var currentDestination by remember { mutableStateOf(NavDestination.HOME) }
    var destinationStack by remember { mutableStateOf(listOf(NavDestination.HOME)) }

    val auth = Firebase.auth
    var currentUser by remember { mutableStateOf(auth.currentUser) }

    val userProfile by if (currentUser != null) {
        repository.observeUserProfile(currentUser!!.uid).collectAsState(initial = null)
    } else {
        remember { mutableStateOf(null) }
    }

    val isUserAdmin = userProfile?.isStaff() == true ||
            currentUser?.email?.equals("chahidahmed80@gmail.com", ignoreCase = true) == true

    fun navigateTo(destination: NavDestination) {
        if (currentDestination != destination) {
            currentDestination = destination
            destinationStack = destinationStack + destination
        }
    }

    fun navigateBack(): Boolean {
        if (destinationStack.size > 1) {
            val updated = destinationStack.dropLast(1)
            destinationStack = updated
            currentDestination = updated.last()
            return true
        } else if (currentDestination != NavDestination.HOME) {
            currentDestination = NavDestination.HOME
            destinationStack = listOf(NavDestination.HOME)
            return true
        }
        return false
    }

    BackHandler(enabled = currentDestination != NavDestination.HOME) {
        navigateBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentDestination != NavDestination.ADMIN) {
                PedrosTopBar(
                    onCartClicked = { navigateTo(NavDestination.CART) },
                    onAdminClicked = { navigateTo(NavDestination.ADMIN) },
                    isAdminUser = isUserAdmin,
                    onLanguageSelected = onLanguageSelected
                )
            }
        },
        bottomBar = {
            if (currentDestination != NavDestination.ADMIN && currentDestination != NavDestination.CART) {
                PedrosBottomBar(
                    currentDestination = currentDestination,
                    onNavigate = { navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        when (currentDestination) {
            NavDestination.HOME -> HomeScreen(
                repository = repository,
                onNavigate = { navigateTo(it) },
                modifier = Modifier.padding(innerPadding)
            )
            NavDestination.MENU -> MenuScreen(
                repository = repository,
                modifier = Modifier.padding(innerPadding)
            )
            NavDestination.OFFERS -> OffersScreen(
                repository = repository,
                modifier = Modifier.padding(innerPadding)
            )
            NavDestination.ORDERS -> OrdersScreen(
                repository = repository,
                onNavigate = { navigateTo(it) },
                modifier = Modifier.padding(innerPadding)
            )
            NavDestination.ACCOUNT -> AccountScreen(
                repository = repository,
                onNavigate = { navigateTo(it) },
                onLanguageSelected = onLanguageSelected,
                modifier = Modifier.padding(innerPadding)
            )
            NavDestination.CART -> CartScreen(
                repository = repository,
                onNavigate = { navigateTo(it) },
                modifier = Modifier.padding(innerPadding)
            )
            NavDestination.ADMIN -> AdminScreen(
                repository = repository,
                onBackClicked = { navigateBack() },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
