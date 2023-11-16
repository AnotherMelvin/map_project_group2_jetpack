package id.ac.umn.stevenindriano.map_project_group2

import android.content.Context
import id.ac.umn.stevenindriano.map_project_group2.database.ExpireListDatabase
import id.ac.umn.stevenindriano.map_project_group2.ui.repository.Repository

object Graph {
    lateinit var db: ExpireListDatabase
        private set

    val repository by lazy {
        Repository(expireListDao = db.listDao())
    }

    fun provide(context: Context) {
        db = ExpireListDatabase.getDatabase(context)
    }
}