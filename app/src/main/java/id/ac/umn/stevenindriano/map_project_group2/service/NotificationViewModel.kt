package id.ac.umn.stevenindriano.map_project_group2.service

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.UUID
import java.util.concurrent.TimeUnit

class NotificationViewModel(application: Application): ViewModel() {
    private val workManager = WorkManager.getInstance(application)

    internal fun scheduleNotifications(
        durationReminder: Long,
        durationExp: Long,
        unit: TimeUnit,
        itemName: String,
        exp: String
    ): Pair<UUID,UUID> {
        val reminderWorkRequestBuilder = OneTimeWorkRequestBuilder<NotificationReminderWorker>()
        val expireWorkRequestBuilder = OneTimeWorkRequestBuilder<NotificationExpireWorker>()

        reminderWorkRequestBuilder.setInputData(
            workDataOf(
                "ITEM" to itemName,
                "EXP" to exp
            )
        )

        expireWorkRequestBuilder.setInputData(
            workDataOf(
                "ITEM" to itemName
            )
        )

        reminderWorkRequestBuilder.setInitialDelay(durationReminder, unit)
        expireWorkRequestBuilder.setInitialDelay(durationExp, unit)

        val reminderWorkerBuild = reminderWorkRequestBuilder.build()
        val expWorkerBuild = expireWorkRequestBuilder.build()

        workManager.enqueue(reminderWorkerBuild)
        workManager.enqueue(expWorkerBuild)

        Log.e("ReminderWorker", reminderWorkerBuild.id.toString())
        Log.e("ExpWorker", expWorkerBuild.id.toString())
        return Pair(reminderWorkerBuild.id, expWorkerBuild.id)
    }

    internal fun cancelWorker(reminder: UUID, exp: UUID) {
        workManager.cancelWorkById(reminder)
        workManager.cancelWorkById(exp)
    }
}

class NotificationViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            NotificationViewModel(application) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}