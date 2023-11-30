package id.ac.umn.stevenindriano.map_project_group2.ui.repository

import id.ac.umn.stevenindriano.map_project_group2.database.ExpireList
import id.ac.umn.stevenindriano.map_project_group2.database.ExpireListDao
import kotlinx.coroutines.flow.Flow

class Repository(private val expireListDao: ExpireListDao) {
    fun getItem(id: Int) = expireListDao.getItem(id)
    fun getAllItems(): Flow<List<ExpireList>> = expireListDao.getAllItems()
    suspend fun insertItem(item: ExpireList) {
        expireListDao.insert(item)
    }

    suspend fun updateItem(item: ExpireList) {
        expireListDao.update(item)
    }

    suspend fun deleteItem(id: Int) {
        expireListDao.delete(id)
    }
}