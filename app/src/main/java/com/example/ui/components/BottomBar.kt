package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.i18n.Strings

enum class NavDestination(val route: String) {
    HOME("home"),
    MENU("menu"),
    OFFERS("offers"),
    ORDERS("orders"),
    ACCOUNT("account"),
    CART("cart"),
    ADMIN("admin")
}

@Composable
fun PedrosBottomBar(
    currentDestination: NavDestination,
    onNavigate: (NavDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentDestination == NavDestination.HOME,
            onClick = { onNavigate(NavDestination.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentDestination == NavDestination.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text(Strings.get("nav_home"), fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_home_tab")
        )

        NavigationBarItem(
            selected = currentDestination == NavDestination.MENU,
            onClick = { onNavigate(NavDestination.MENU) },
            icon = {
                Icon(
                    imageVector = if (currentDestination == NavDestination.MENU) Icons.Filled.RestaurantMenu else Icons.Outlined.RestaurantMenu,
                    contentDescription = "Menu"
                )
            },
            label = { Text(Strings.get("nav_menu"), fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_menu_tab")
        )

        NavigationBarItem(
            selected = currentDestination == NavDestination.OFFERS,
            onClick = { onNavigate(NavDestination.OFFERS) },
            icon = {
                Icon(
                    imageVector = if (currentDestination == NavDestination.OFFERS) Icons.Filled.LocalOffer else Icons.Outlined.LocalOffer,
                    contentDescription = "Offers"
                )
            },
            label = { Text(Strings.get("nav_offers"), fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_offers_tab")
        )

        NavigationBarItem(
            selected = currentDestination == NavDestination.ORDERS,
            onClick = { onNavigate(NavDestination.ORDERS) },
            icon = {
                Icon(
                    imageVector = if (currentDestination == NavDestination.ORDERS) Icons.Filled.ReceiptLong else Icons.Outlined.ReceiptLong,
                    contentDescription = "Orders"
                )
            },
            label = { Text(Strings.get("nav_orders"), fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_orders_tab")
        )

        NavigationBarItem(
            selected = currentDestination == NavDestination.ACCOUNT,
            onClick = { onNavigate(NavDestination.ACCOUNT) },
            icon = {
                Icon(
                    imageVector = if (currentDestination == NavDestination.ACCOUNT) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Account"
                )
            },
            label = { Text(Strings.get("nav_account"), fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_account_tab")
        )
    }
}
