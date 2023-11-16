package id.ac.umn.stevenindriano.map_project_group2.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.umn.stevenindriano.map_project_group2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    CenteredText(text = "No items yet")
    AddButton()
}

@Composable
fun AddButton() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
    ){
        Row(modifier = Modifier
            .align(Alignment.BottomEnd)){
            FloatingActionButton(modifier = Modifier
                .size(64.dp),
                onClick = { /*do something*/ },) {
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

@Preview
@Composable
fun PreviewHome() {
    HomeScreen()
}