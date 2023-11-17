package id.ac.umn.stevenindriano.map_project_group2.ui.createedit

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import id.ac.umn.stevenindriano.map_project_group2.ui.home.ExpireItems
import id.ac.umn.stevenindriano.map_project_group2.ui.home.formatDate
import id.ac.umn.stevenindriano.map_project_group2.ui.navigation.NavScreenMenu
import java.util.Calendar
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditScreen(
    id: Int,
    navigateUp: () -> Unit
) {
    val viewModel = viewModel<CreateEditViewModel>(factory = CreateEditViewModelFactory(id))

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(modifier = Modifier
                .size(64.dp),
                onClick = {

                },) {
                Icon(Icons.Filled.Delete, contentDescription = "Remove Button")
            }
        }
    ) {
        ItemEntry(
            state = viewModel.state,
            onNameChange = viewModel::onNameChange,
            onLocationChange = viewModel::onLocationChange,
            onQtyChange = viewModel::onQtyChange,
            onNotesChange = viewModel::onNotesChange,
            onDateSelected = viewModel::onDateChange,
            updateItem = { viewModel.updateListItem(id) },
            saveItem = viewModel::addListItem
        ) {
            navigateUp.invoke()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemEntry(
    modifier: Modifier = Modifier,
    state: CreateEditState,
    onNameChange:(String) -> Unit,
    onLocationChange:(String) -> Unit,
    onQtyChange:(String) -> Unit,
    onNotesChange:(String) -> Unit,
    onDateSelected:(Date) -> Unit,
    updateItem:() -> Unit,
    saveItem:() -> Unit,
    navigateUp:() -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = state.name,
            onValueChange = { onNameChange(it) },
            label = { Text(text = "Item Name") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.Companion.size(15.dp))
        OutlinedTextField(
            value = state.location,
            onValueChange = { onLocationChange(it) },
            label = { Text(text = "Item Location") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.Companion.size(15.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = formatDate(state.date))
                Spacer(modifier = Modifier.size(4.dp))
                val mDatePicker = datePickerDialog(
                    context = LocalContext.current,
                    onDateSelected = { date ->
                        onDateSelected.invoke(date)
                    }
                )
                IconButton(onClick = { mDatePicker.show() }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null)
                }
            }
            OutlinedTextField(
                value = state.qty,
                onValueChange = { onQtyChange(it) },
                label = { Text(text = "Qty") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Spacer(modifier = Modifier.Companion.size(15.dp))
        OutlinedTextField(
            value = state.notes,
            onValueChange = { onNotesChange(it) },
            label = { Text(text = "Notes") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )
        Spacer(modifier = Modifier.Companion.size(15.dp))
        val buttonTitle = if (state.isUpdatingItem) "Update" else "Add"
        Button(onClick = {
            when(state.isUpdatingItem) {
                true -> {
                    updateItem.invoke()
                }
                false -> {
                    saveItem.invoke()
                }
            }
            navigateUp.invoke()
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = state.name.isNotEmpty() &&
                state.qty.isNotEmpty() &&
                state.date.toString().isNotEmpty()
        ) {
            Text(text = buttonTitle)
        }
    }
}

@Composable
fun datePickerDialog(
    context: Context,
    onDateSelected: (Date) -> Unit
): DatePickerDialog {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val mDatePickerDialog = DatePickerDialog(
        context,
        {_:DatePicker, mYear: Int, mMonth: Int, mDayofMonth: Int ->
            val calender = Calendar.getInstance()
            calender.set(mYear, mMonth, mDayofMonth)
            onDateSelected.invoke(calender.time)
        }, year, month, day
    )

    return mDatePickerDialog
}