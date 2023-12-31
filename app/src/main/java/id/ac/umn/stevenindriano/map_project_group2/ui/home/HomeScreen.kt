package id.ac.umn.stevenindriano.map_project_group2.ui.home

import android.annotation.SuppressLint
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import id.ac.umn.stevenindriano.map_project_group2.R
import id.ac.umn.stevenindriano.map_project_group2.database.ExpireList
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (Int) -> Unit,
    sortList: String,
    onBack: () -> Unit
) {
    val homeViewModel: HomeViewModel = viewModel()
    val homeState by homeViewModel.state.collectAsState(emptyList())
    var sortedList = homeState

    BackHandler(onBack = onBack)

    LaunchedEffect(sortList, Unit) {
        sortedList = when (sortList) {
            "Oldest" -> homeState.sortedBy { it.exp.time }
            "Newest" -> homeState.sortedByDescending { it.exp.time }
            else -> homeState
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .size(64.dp),
                onClick = {
                    onNavigate.invoke(-1)
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Button")
            }
        }
    ) {
        LazyColumn {
            items(sortedList) {
                ExpireItems(
                    item = it
                ) {
                    onNavigate.invoke(it.id)
                }
            }
            item { Spacer(modifier = Modifier.padding(100.dp)) }
        }
    }

    if (sortedList.isEmpty()) {
        CenteredText(text = "No items yet")
    }
}

@Composable
fun CenteredText(text: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text, style = MaterialTheme.typography.displaySmall)
    }
}

@Composable
fun ExpireItems(
    item: ExpireList,
    onItemClick: () -> Unit
) {
    var expand by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                expand = !expand
            }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.size(5.dp))
                if (item.imagePath != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = item.imagePath),
                        contentDescription = "Item Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.groceries),
                        contentDescription = "Item Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .width(150.dp)
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Divider(
                        modifier = Modifier.width(150.dp),
                        thickness = 1.dp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = formatDate(item.exp),
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = "Location: " + item.location,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = "Qty: " + item.qty,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!expand) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Expand"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Shrink"
                        )
                    }
                }
            }

            if (expand) {
                Spacer(modifier = Modifier.size(4.dp))
                Divider(thickness = 1.dp, color = Color.White)
                Spacer(modifier = Modifier.size(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.size(16.dp))
                    Box(modifier = Modifier.width(290.dp)) {
                        Text(
                            text = "Notes: \n" + item.notes,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier
                                .clickable {
                                    onItemClick()
                                },
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    }
                }
                Spacer(modifier = Modifier.size(10.dp))
            }
        }
    }
}

fun formatDate(date: Date): String =
    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)

@Composable
fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    val currentOnBack by rememberUpdatedState(onBack)
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }

    SideEffect {
        backCallback.isEnabled = enabled
    }
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}

//@Preview
//@Composable
//fun PreviewHome() {
//    val navController = rememberNavController()
//    HomeScreen(navController)
//}