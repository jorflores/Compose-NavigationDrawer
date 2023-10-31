package com.example.basicnavigationdrawer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch


data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(){



    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val selectedItemIndex = rememberSaveable {
        mutableStateOf(0)
    }

    val scope = rememberCoroutineScope()

    val items =  mutableListOf(
        NavigationItem(
            title = "HomePage",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = "HomePage"
        ),
        NavigationItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,
            route = "SettingsPage"
        ),
        NavigationItem(
            title = "Game",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            route = "GamePage"
        )
    )


    ModalNavigationDrawer(drawerContent = {

        ModalDrawerSheet {

            Spacer(modifier = Modifier.height(16.dp))

            items.forEachIndexed { index, item ->
                NavigationDrawerItem(
                    label = {
                        Text(text = item.title)
                    },
                    selected = index == selectedItemIndex.value,
                    onClick = {
                        navController.navigate(item.route)
                        selectedItemIndex.value = index
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (index == selectedItemIndex.value) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    },
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }

        }
    }, drawerState = drawerState) {

        // Aaqui va el contenido

        Scaffold(topBar = {
            TopAppBar(title = { Text(text = "My App") }, navigationIcon = {

                IconButton(onClick = {

                    if (drawerState.isClosed) {
                        scope.launch {
                            drawerState.open()
                        }
                    } else {
                        scope.launch {
                            drawerState.close()
                        }
                    }


                }) {
                    Icon(Icons.Filled.Menu, contentDescription = "Drawer Menu.")
                }

            })
        }) {

            Box(modifier = Modifier.padding(it)) {
                NavHost(navController = navController, startDestination = "HomePage") {

                    composable("HomePage") {
                        HomePage()
                    }

                    composable("SettingsPage") {
                        SettingsPage()
                    }

                    composable("GamePage") {
                        GamePage()
                    }

                }

            }
        }
    }

}