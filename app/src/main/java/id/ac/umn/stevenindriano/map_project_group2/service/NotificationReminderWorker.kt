package id.ac.umn.stevenindriano.map_project_group2.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.work.Worker
import androidx.work.WorkerParameters
import id.ac.umn.stevenindriano.map_project_group2.MainActivity
import id.ac.umn.stevenindriano.map_project_group2.MainApplication
import id.ac.umn.stevenindriano.map_project_group2.R

class NotificationReminderWorker(
    context: Context,
    workerParams: WorkerParameters
): Worker(context, workerParams) {
    companion object {
        const val nameKeyItem = "ITEM"
        const val nameKeyExp = "EXP"
    }

    private val notificationId = 230

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "HOME/FROM={NOTIFICATION}".toUri(),
            applicationContext,
            MainActivity::class.java)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val itemName = inputData.getString(nameKeyItem)
        val exp = inputData.getString(nameKeyExp)
        val body = "$itemName will be expired in $exp!"

        val builder = NotificationCompat.Builder(applicationContext, MainApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo_xpiry)
            .setContentTitle("Watch Out!")
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build())
        }

        return Result.success()
    }
}