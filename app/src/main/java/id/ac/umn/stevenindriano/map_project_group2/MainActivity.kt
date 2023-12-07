package id.ac.umn.stevenindriano.map_project_group2

import android.Manifest
import android.accounts.AccountManager
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.google.android.gms.auth.api.identity.Identity
import id.ac.umn.stevenindriano.map_project_group2.auth.GoogleAuthUiClient
import id.ac.umn.stevenindriano.map_project_group2.ui.createedit.CreateEditScreen
import id.ac.umn.stevenindriano.map_project_group2.ui.createedit.CreateEditViewModel
import id.ac.umn.stevenindriano.map_project_group2.ui.createedit.CreateEditViewModelFactory
import id.ac.umn.stevenindriano.map_project_group2.ui.home.HomeScreen
import id.ac.umn.stevenindriano.map_project_group2.ui.home.HomeViewModel
import id.ac.umn.stevenindriano.map_project_group2.ui.landing.LandingScreen
import id.ac.umn.stevenindriano.map_project_group2.ui.landing.LandingViewModel
import id.ac.umn.stevenindriano.map_project_group2.ui.navigation.NavDrawerMenu
import id.ac.umn.stevenindriano.map_project_group2.ui.navigation.NavScreenMenu
import id.ac.umn.stevenindriano.map_project_group2.ui.setting.SettingScreen
import id.ac.umn.stevenindriano.map_project_group2.ui.theme.Map_project_group2Theme
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

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

    private fun goHome() {
        val i = Intent(Intent.ACTION_MAIN)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.addCategory(Intent.CATEGORY_HOME)
        startActivity(i)
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
                var itemId by remember {
                    mutableIntStateOf(0)
                }
                var sortList by remember {
                    mutableStateOf("Newest")
                }
                var isHome by remember {
                    mutableStateOf(false)
                }
                var isSettings by remember {
                    mutableStateOf(false)
                }
                var isCreate by remember {
                    mutableStateOf(false)
                }
                var isEdit by remember {
                    mutableStateOf(false)
                }
                var isSign by remember {
                    mutableStateOf(false)
                }

                val createEditViewModel = viewModel<CreateEditViewModel>(factory = CreateEditViewModelFactory(itemId))

                var openAlertDialog by rememberSaveable {
                    mutableStateOf(false)
                }

                if (openAlertDialog) {
                    AlertDialog(onDismissRequest = {
                        openAlertDialog = false
                    }, confirmButton = {
                        TextButton(onClick = {
                            createEditViewModel.deleteItem(itemId)
                            navController.popBackStack()
                            openAlertDialog = false
                        }) {
                            Text(text = "Yes")
                        }
                    },
                        title = {
                            Text(text = "Confirm Delete")
                        },
                        text = {
                            Text(text = "Do you want to delete this item?")
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                openAlertDialog = false
                            }) {
                                Text(text = "No")
                            }
                        }
                    )
                }

                val context = LocalContext.current
                val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)
                val landViewModel = viewModel<LandingViewModel>()
                val state by landViewModel.state.collectAsStateWithLifecycle()

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
                            if ((isSign) && (isHome || isSettings)) {
                                ModalDrawerSheet {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                                    ) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            if (googleAuthUiClient.getSignedInUser() != null) {
                                                if (googleAuthUiClient.getSignedInUser()!!.profilePictureUrl != null) {
                                                    AsyncImage(
                                                        model = googleAuthUiClient.getSignedInUser()!!.profilePictureUrl,
                                                        contentDescription = "Profile Picture",
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier
                                                            .size(64.dp)
                                                            .clip(CircleShape)
                                                    )
                                                } else {
                                                    Image(
                                                        painter = painterResource(id = R.drawable.xpiry_logo),
                                                        contentDescription = "app logo",
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier
                                                            .size(64.dp)
                                                            .clip(RoundedCornerShape(16.dp))
                                                    )
                                                }

                                                Spacer(modifier = Modifier.width(16.dp))

                                                if (googleAuthUiClient.getSignedInUser()!!.username != null) {
                                                    Text(
                                                        text = "Hello, " + (googleAuthUiClient.getSignedInUser()?.username ?: String),
                                                        fontSize = 18.sp,
                                                        fontWeight = Bold
                                                    )
                                                } else {
                                                    Text(
                                                        text = "Xpiry",
                                                        fontSize = 18.sp,
                                                        fontWeight = Bold
                                                    )
                                                }
                                            } else {
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
                                                    text = "Xpiry",
                                                    fontSize = 18.sp,
                                                    fontWeight = Bold
                                                )
                                            }
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
                            }
                        },
                        drawerState = drawerState
                    ) {
                        Scaffold(
                            topBar = {
                                if (isSign) {
                                    TopAppBar(
                                        title = {
                                            if (isCreate) {
                                                Text(text = "Add New Item")
                                            } else if (isEdit) {
                                                Text(text = "Update Item")
                                            } else if (isSettings) {
                                                Text(text = "Settings")
                                            } else {
                                                Text(text = getString(R.string.app_name))
                                            }
                                        },
                                        navigationIcon = {
                                            if (isCreate || isEdit) {
                                                IconButton(onClick = {
                                                    navController.popBackStack()
                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Default.ArrowBack,
                                                        contentDescription = "Back"
                                                    )
                                                }
                                            } else {
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
                                            }
                                        },
                                        actions = {
                                            Box(
                                                modifier = Modifier
                                                    .wrapContentSize(Alignment.TopEnd)
                                            ) {
                                                if (isHome) {
                                                    IconButton(onClick = { menuExpanded = !menuExpanded }) {
                                                        Image(painter = painterResource(id = R.drawable.ic_filter), contentDescription = "")
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
                                                } else if (isEdit) {
                                                    IconButton(onClick = {
                                                        openAlertDialog = true
                                                    }) {
                                                        Icon(Icons.Filled.Delete, contentDescription = "Remove Button")
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        ) { innerPaddingValues ->
                            NavHost(
                                navController = navController,
                                startDestination = NavScreenMenu.Landing.route,
                                modifier = Modifier
                                    .padding(innerPaddingValues)
                                    .animateContentSize()
                            ) {
                                composable(route = NavScreenMenu.Landing.route) {
                                    LaunchedEffect(key1 = Unit) {
                                        if (googleAuthUiClient.getSignedInUser() != null) {
                                            navController.navigate(NavDrawerMenu.Home.route)
                                        }
                                    }

                                    val launcher = rememberLauncherForActivityResult(
                                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                                        onResult = { result ->
                                            if (result.resultCode == RESULT_OK) {
                                                lifecycleScope.launch {
                                                    val signInResult = googleAuthUiClient.signInWithIntent(
                                                        intent = result.data ?: return@launch
                                                    )
                                                    landViewModel.onSignInResult(signInResult)
                                                }
                                            }
                                        }
                                    )

                                    LaunchedEffect(key1 = state.isSignInSucessful) {
                                        if (state.isSignInSucessful) {
                                            isSign = true
                                            Toast.makeText(applicationContext, "Sign in successful", Toast.LENGTH_LONG).show()
                                            navController.navigate(NavDrawerMenu.Home.route)
                                            landViewModel.resetState()
                                        }
                                    }

                                    LandingScreen(
                                        state = state,
                                        onSignInCLick = {mode ->
                                            if (mode == 1) {
                                                lifecycleScope.launch {
                                                    val signInIntentSender = googleAuthUiClient.signIn()
                                                    launcher.launch(
                                                        IntentSenderRequest.Builder(
                                                            signInIntentSender ?: return@launch
                                                        ).build()
                                                    )
                                                }
                                            } else {
                                                isSign = true
                                                Toast.makeText(applicationContext, "Guest mode", Toast.LENGTH_LONG).show()
                                                navController.navigate(NavDrawerMenu.Home.route)
                                            }
                                        }
                                    )

                                    isHome = false
                                    isSettings = false
                                    isCreate = false
                                    isEdit = false
                                }
                                composable(route = NavDrawerMenu.Home.route) {
                                    HomeScreen(
                                        onNavigate = {id ->
                                            navController.navigate(route = "${NavScreenMenu.CreateEdit.route}?id=$id")
                                            itemId = id
                                        },
                                        sortList,
                                        onBack = {
                                            goHome()
                                        }
                                    )

                                    isHome = true
                                    isSettings = false
                                    isCreate = false
                                    isEdit = false
                                }
                                composable(
                                    route = "${NavScreenMenu.CreateEdit.route}?id={id}",
                                    arguments = listOf(navArgument("id"){type = NavType.IntType})
                                ) {
                                    val id = it.arguments?.getInt("id") ?: -1
                                    CreateEditScreen(id = id, requestPermissionLauncher = requestPermissionLauncher) {
                                        navController.navigateUp()
                                    }

                                    isHome = false
                                    isSettings = false

                                    if (id == -1) {
                                        isCreate = true
                                    } else {
                                        isEdit = true
                                    }
                                }
                                composable(route = NavDrawerMenu.Setting.route) {
                                    SettingScreen(
                                        onSignOut = {
                                            lifecycleScope.launch {
                                                googleAuthUiClient.signOut()
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Signed out",
                                                    Toast.LENGTH_LONG
                                                ).show()

                                                isSign = false
                                                navController.popBackStack()
                                            }
                                        }
                                    )

                                    isHome = false
                                    isSettings = true
                                    isCreate = false
                                    isEdit = false
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}