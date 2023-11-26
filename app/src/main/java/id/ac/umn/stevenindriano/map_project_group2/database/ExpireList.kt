package id.ac.umn.stevenindriano.map_project_group2.database

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "item_list")
data class ExpireList(
    @ColumnInfo(name = "item_id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "item_name")
    val name: String,
    @ColumnInfo(name = "item_location")
    val location: String,
    @ColumnInfo(name = "item_qty")
    val qty: String,
    @ColumnInfo(name = "item_exp")
    val exp: Date,
    @ColumnInfo(name = "item_notes")
    val notes: String,
    @ColumnInfo(name = "image")
    val imagePath: Uri? = null,
)
