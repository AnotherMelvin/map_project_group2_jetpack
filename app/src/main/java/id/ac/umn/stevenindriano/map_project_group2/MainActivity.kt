package id.ac.umn.stevenindriano.map_project_group2

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import id.ac.umn.stevenindriano.map_project_group2.ui.home.HomeViewModel
import id.ac.umn.stevenindriano.map_project_group2.ui.navigation.NavDrawerMenu
import id.ac.umn.stevenindriano.map_project_group2.ui.theme.Map_project_group2Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) showRationaleDialog()
    }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // granted
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                navigateToAppSettings()
            }

            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun showRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("The camera permission is required to take pictures.")
            .setPositiveButton("OK") { _, _ ->
                navigateToAppSettings()
            }
            .setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun navigateToAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Graph.provide(this)
        requestCameraPermission()
        setContent {
            Map_project_group2Theme {
                val navDrawerMenu = listOf(
                    NavDrawerMenu.Home,
                    NavDrawerMenu.Setting,
                )

                val navController = rememberNavController()

                var menuExpanded by remember {
                    mutableStateOf(false)
                }

                val context = LocalContext.current

                val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val coroutineScope = rememberCoroutineScope()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                                ) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = R.drawable.xpiry_logo),
                                            contentDescription = "app logo",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(64.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                        )

                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = stringResource(id = R.string.app_name),
                                            fontSize = 18.sp,
                                            fontWeight = Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Divider(thickness = 1.dp)
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                                navDrawerMenu.forEach { item ->
                                    NavigationDrawerItem(
                                        label = { Text(text = stringResource(item.label)) },
                                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                                        onClick = {
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                            coroutineScope.launch {
                                                drawerState.close()
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (currentDestination?.hierarchy?.any { it.route == item.route } != true) item.icon else item.selectedIcon,
                                                contentDescription = stringResource(item.label)
                                            )
                                        },
                                        badge = {
                                            item.badgeCount?.let {
                                                Text(text = item.badgeCount.toString())
                                            }
                                        },
                                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                    )
                                }
                            }
                        },
                        drawerState = drawerState
                    ) {

                        var sortList by remember {
                            mutableStateOf("Newest")
                        }

                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = {
                                        Text(text = getString(R.string.app_name))
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = {
                                            coroutineScope.launch {
                                                drawerState.open()
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Menu,
                                                contentDescription = "Menu"
                                            )
                                        }
                                    },
                                    actions = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentSize(Alignment.TopEnd)
                                        ) {
                                            IconButton(onClick = { menuExpanded = !menuExpanded }) {
                                                Icon(
                                                    imageVector = Icons.Default.MoreVert,
                                                    contentDescription = "More"
                                                )
                                            }
                                            DropdownMenu(
                                                expanded = menuExpanded,
                                                onDismissRequest = { menuExpanded = false }
                                            ) {
                                                DropdownMenuItem(
                                                    text = { Text("Newest") },
                                                    onClick = {
                                                        sortList = "Newest"
                                                        menuExpanded = false
                                                    }
                                                )
                                                DropdownMenuItem(
                                                    text = { Text("Oldest") },
                                                    onClick = {
                                                        sortList = "Oldest"
                                                        menuExpanded = false
                                                    }
                                                )
                                            }
                                        }


                                    }
                                )
                            }
                        ) { innerPaddingValues ->
                            MobileNavGraph(
                                navController = navController,
                                innerPaddingValues = innerPaddingValues,
                                sortList = sortList,
                                requestPermissionLauncher = requestPermissionLauncher
                            )
                        }
                    }
                }
            }
        }
    }

}