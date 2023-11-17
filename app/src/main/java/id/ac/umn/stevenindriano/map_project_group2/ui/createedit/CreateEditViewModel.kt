package id.ac.umn.stevenindriano.map_project_group2.ui.createedit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import id.ac.umn.stevenindriano.map_project_group2.Graph
import id.ac.umn.stevenindriano.map_project_group2.database.ExpireList
import id.ac.umn.stevenindriano.map_project_group2.ui.repository.Repository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

class CreateEditViewModel
constructor(
    private val itemId: Int,
    private val repo: Repository = Graph.repository
): ViewModel() {

    var state by mutableStateOf(CreateEditState())
        private set

    init {
        if (itemId != -1) {
            viewModelScope.launch {
                repo.getItem(itemId).collectLatest {
                    state = state.copy(
                        name = it.name,
                        location = it.location,
                        qty = it.qty,
                        date = it.exp,
                        notes = it.notes
                    )
                }
            }
        }
    }

    init {
        state = if (itemId != -1) {
            state.copy(isUpdatingItem = true)
        } else {
            state.copy(isUpdatingItem = false)
        }
    }

    val isFieldNotEmpty: Boolean
        get() = state.name.isNotEmpty() &&
                state.qty.isNotEmpty() &&
                state.date.toString().isNotEmpty()

    fun onNameChange(newValue: String) {
        state = state.copy(name = newValue)
    }

    fun onLocationChange(newValue: String) {
        state = state.copy(location = newValue)
    }

    fun onQtyChange(newValue: String) {
        state = state.copy(qty = newValue)
    }

    fun onDateChange(newValue: Date) {
        state = state.copy(date = newValue)
    }

    fun onNotesChange(newValue: String) {
        state = state.copy(notes = newValue)
    }

    fun addListItem() {
        viewModelScope.launch {
            repo.insertItem(
                ExpireList(
                    name = state.name,
                    location = state.location,
                    qty = state.qty,
                    exp = state.date,
                    notes = state.notes
                )
            )
        }
    }

    fun updateListItem(id: Int) {
        viewModelScope.launch {
            repo.insertItem(
                ExpireList(
                    id = id,
                    name = state.name,
                    location = state.location,
                    qty = state.qty,
                    exp = state.date,
                    notes = state.notes
                )
            )
        }
    }
}

@Suppress("UNCHECKED_CAST")
class CreateEditViewModelFactory(private val id: Int): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return CreateEditViewModel(itemId = id) as T
    }
}

data class CreateEditState(
    val name: String = "",
    val location: String = "",
    val qty: String = "",
    val notes: String = "",
    val date: Date = Date(),
    val isUpdatingItem: Boolean = false
)