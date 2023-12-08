package id.ac.umn.stevenindriano.map_project_group2.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.work.Worker
import androidx.work.WorkerParameters
import id.ac.umn.stevenindriano.map_project_group2.MainActivity
import id.ac.umn.stevenindriano.map_project_group2.MainApplication
import id.ac.umn.stevenindriano.map_project_group2.R

class NotificationExpireWorker(
    context: Context,
    workerParams: WorkerParameters
): Worker(context, workerParams) {
    companion object {
        const val nameKeyItem = "ITEM"
    }

    private val notificationId = 231
    private var cancel = false

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
        val body = "$itemName is expired!"

        val builder = NotificationCompat.Builder(applicationContext, MainApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo_xpiry)
            .setContentTitle("Warning!")
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (!cancel) {
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(notificationId, builder.build())
            }
        }

        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        cancel = true
    }
}