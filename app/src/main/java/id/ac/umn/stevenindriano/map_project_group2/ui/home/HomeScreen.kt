package id.ac.umn.stevenindriano.map_project_group2.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import id.ac.umn.stevenindriano.map_project_group2.database.ExpireList
import id.ac.umn.stevenindriano.map_project_group2.ui.navigation.NavScreenMenu
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)
    val homeState = homeViewModel.state

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(modifier = Modifier
                .size(64.dp),
                onClick = {
                    navController.navigate(NavScreenMenu.Create.route)
                },) {
                Icon(Icons.Filled.Add, contentDescription = "Add Button")
            }
        }
    ) {
        LazyColumn {
            items(homeState.items) {
                ExpireItems(
                    item = it,
                ) {
                    navController.navigate(NavScreenMenu.Edit.route)
                }
            }
        }
    }

//    CenteredText(text = "No items yet")
//    AddButton(navController)
}

@Composable
fun AddButton(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
    ){
        Row(modifier = Modifier
            .align(Alignment.BottomEnd)){
            FloatingActionButton(modifier = Modifier
                .size(64.dp),
                onClick = {
                    navController.navigate(NavScreenMenu.Create.route)
                },) {
                Icon(Icons.Filled.Add, contentDescription = "Add Button")
            }
        }
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClick.invoke()
            }
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = item.location,
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = item.qty.toString(),
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = formatDate(item.exp),
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = item.notes,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

fun formatDate(date: Date): String =
    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)

@Preview
@Composable
fun PreviewHome() {
    val navController = rememberNavController()
    HomeScreen(navController)
}