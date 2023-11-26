package id.ac.umn.stevenindriano.map_project_group2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpireListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ExpireList)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: ExpireList)

    @Delete()
    suspend fun delete(item: ExpireList)

    @Query("SELECT * FROM item_list")
    fun getAllItems(): Flow<List<ExpireList>>

    @Query("SELECT * FROM item_list WHERE item_id = :itemId")
    fun getItem(itemId: Int): Flow<ExpireList>
}