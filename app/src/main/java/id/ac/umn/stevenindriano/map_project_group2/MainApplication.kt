package id.ac.umn.stevenindriano.map_project_group2

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import id.ac.umn.stevenindriano.map_project_group2.ui.setting.SettingWrapper

class MainApplication: Application() {
    companion object {
        const val CHANNEL_ID = "xpiry_reminder_id"
    }

    lateinit var settingWrapper: SettingWrapper

    override fun onCreate() {
        super.onCreate()

        settingWrapper = SettingWrapper(
            getSharedPreferences(
                "reminder",
                Context.MODE_PRIVATE
            )
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}