package id.ac.umn.stevenindriano.map_project_group2.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.umn.stevenindriano.map_project_group2.Graph
import id.ac.umn.stevenindriano.map_project_group2.database.ExpireList
import id.ac.umn.stevenindriano.map_project_group2.ui.repository.Repository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: Repository = Graph.repository): ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    init {
        getAllItems()
    }

    private fun getAllItems() {
        viewModelScope.launch {
            repo.getAllItems().collectLatest {
                state = state.copy(
                    items = it
                )
            }
        }
    }

    fun deleteItem(item: ExpireList) {
        viewModelScope.launch {
            repo.deleteItem(item)
        }
    }
}

data class HomeState(
    val items: List<ExpireList> = emptyList()
)