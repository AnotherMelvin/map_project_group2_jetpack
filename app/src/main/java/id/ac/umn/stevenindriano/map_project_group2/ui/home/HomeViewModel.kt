package id.ac.umn.stevenindriano.map_project_group2.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.umn.stevenindriano.map_project_group2.Graph
import id.ac.umn.stevenindriano.map_project_group2.database.ExpireList
import id.ac.umn.stevenindriano.map_project_group2.ui.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: Repository = Graph.repository) : ViewModel() {

    val state: Flow<List<ExpireList>> = repo.getAllItems()

    fun deleteItem(id: Int) {
        viewModelScope.launch {
            repo.deleteItem(id)
        }
    }
}