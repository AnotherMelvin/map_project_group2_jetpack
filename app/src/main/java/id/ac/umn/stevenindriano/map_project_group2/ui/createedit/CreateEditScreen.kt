package id.ac.umn.stevenindriano.map_project_group2.ui.createedit

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import id.ac.umn.stevenindriano.map_project_group2.R
import id.ac.umn.stevenindriano.map_project_group2.ui.home.formatDate
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditScreen(
    id: Int,
    requestPermissionLauncher: ActivityResultLauncher<String>,
    navigateUp: () -> Unit
) {
    val viewModel = viewModel<CreateEditViewModel>(factory = CreateEditViewModelFactory(id))

    Scaffold() {
        ItemEntry(
            state = viewModel.state,
            requestCameraPermissionLauncher = requestPermissionLauncher,
            onNameChange = viewModel::onNameChange,
            onLocationChange = viewModel::onLocationChange,
            onQtyChange = viewModel::onQtyChange,
            onNotesChange = viewModel::onNotesChange,
            onImageChange = viewModel::onImageChange,
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
    requestCameraPermissionLauncher: ActivityResultLauncher<String>,
    onNameChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onQtyChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onImageChange: (Uri?) -> Unit,
    onDateSelected: (Date) -> Unit,
    updateItem: () -> Unit,
    saveItem: () -> Unit,
    navigateUp: () -> Unit
) {
    val context = LocalContext.current
    var hasImage by remember {
        mutableStateOf(false)
    }

    val launchCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            hasImage = true
        }
    }

    val launchGallery =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) {
            it?.let { uri ->
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                onImageChange(uri)
            }
        }


    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = getCamImageUri(context)
            onImageChange(uri)
            launchCamera.launch(uri)
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }


    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Row {
                    Image(
                        painter = if (state.image != null) rememberAsyncImagePainter(model = state.image) else painterResource(
                            R.drawable.ic_image
                        ),
                        contentDescription = "Item Image",
                        modifier = Modifier.size(200.dp)
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(text = if (state.image != null) "Change Image From:" else "Choose Image from:")
                        OutlinedButton(
                            onClick = {
                                launchGallery.launch(arrayOf("image/*"))
                            }
                        ) {
                            Text(text = "Gallery")
                        }
                        OutlinedButton(
                            onClick = {
                                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        ) {
                            Text(text = "Camera")
                        }
//                        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
//
//                        }
                    }
                }
            }
        }
        Spacer(modifier = modifier.height(15.dp))
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
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
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
        Button(
            onClick = {
                when (state.isUpdatingItem) {
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

fun getCamImageUri(context: Context): Uri? {
    var uri: Uri? = null
    val file = createImageFile(context)
    try {
        uri = FileProvider.getUriForFile(
            context,
            "id.ac.umn.stevenindriano.map_project_group2.fileprovider",
            file
        )
    } catch (e: Exception) {
        Log.e("Camera", "Error: ${e.message}")
    }
    return uri
}

@SuppressLint("SimpleDateFormat")
private fun createImageFile(context: Context): File {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "Xpiry_Image_${timestamp}",
        ".jpg",
        imageDirectory
    )
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
        { _: DatePicker, mYear: Int, mMonth: Int, mDayofMonth: Int ->
            val calender = Calendar.getInstance()
            calender.set(mYear, mMonth, mDayofMonth)
            onDateSelected.invoke(calender.time)
        }, year, month, day
    )

    return mDatePickerDialog
}