package id.ac.umn.stevenindriano.map_project_group2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import id.ac.umn.stevenindriano.map_project_group2.database.converter.DateConverter

@TypeConverters(value = [DateConverter::class])
@Database(
    entities = [ExpireList::class],
    version = 1,
    exportSchema = false
)
abstract class ExpireListDatabase: RoomDatabase() {
    abstract fun listDao(): ExpireListDao

    companion object {
        @Volatile
        var INSTANCE:ExpireListDatabase? = null
        fun getDatabase(context: Context): ExpireListDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ExpireListDatabase::class.java,
                    "expire_db",
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}