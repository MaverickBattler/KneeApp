package ru.leti.kneeapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.leti.kneeapp.activity.StartActivity
import ru.leti.kneeapp.util.SharedPreferencesProvider
import java.util.*

class AlarmReceiverNotificationTraining : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("Info", "Alarm training, message received")

        val i = Intent(context, StartActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, i,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        context?.let {
            val sharedPreferencesProvider = SharedPreferencesProvider(context)
            val sharedPreferences = sharedPreferencesProvider.getEncryptedSharedPreferences()
            val alarmCount = sharedPreferences.getInt("alarm_count", 1)
            val editor = sharedPreferences.edit()
            editor.putInt("alarm_count", alarmCount + 1)
            editor.putString(
                "exercise1_1_pass_mark",
                context.getString(R.string.not_completed)
            )
            editor.putString(
                "exercise1_2_pass_mark",
                context.getString(R.string.not_completed)
            )
            editor.putString(
                "exercise1_3_pass_mark",
                context.getString(R.string.not_completed)
            )
            editor.putString(
                "exercise2_1_pass_mark",
                context.getString(R.string.not_completed)
            )
            editor.putString(
                "exercise3_1_pass_mark",
                context.getString(R.string.not_completed)
            )
            editor.putString(
                "exercise3_2_pass_mark",
                context.getString(R.string.not_completed)
            )
            editor.putString(
                "exercise3_3_pass_mark",
                context.getString(R.string.not_completed)
            )
            editor.putString(
                "exercise4_1_pass_mark",
                context.getString(R.string.not_completed)
            )
            editor.putString(
                "exercise4_2_1_pass_mark",
                context.getString(R.string.not_completed)
            )
            editor.putString(
                "exercise4_2_2_pass_mark",
                context.getString(R.string.not_completed)
            )
            editor.putString(
                "exercise4_2_3_pass_mark",
                context.getString(R.string.not_completed)
            )
            editor.putString(
                "exercise4_3_1_pass_mark",
                context.getString(R.string.not_completed)
            )
            editor.putString(
                "exercise4_3_2_pass_mark",
                context.getString(R.string.not_completed)
            )

            editor.apply()
            val builder = NotificationCompat.Builder(it, it.getString(R.string.channel_id))
                .setContentTitle("KneeApp")
                .setContentText("Не забудьте сегодня выполнить упражнения для реабилитации")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
            with(NotificationManagerCompat.from(it)) {
                // notificationId is a unique int for each notification
                notify(1352, builder.build())
            }

            //Отмена повторяющейся alarm после 7 раз(неделя)
            if (alarmCount == 7) {
                val alarmManager: AlarmManager =
                    context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val alarmIntent: PendingIntent = PendingIntent.getBroadcast(
                    context, 0, Intent(context, AlarmReceiverNotificationTraining::class.java),
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                alarmManager.cancel(alarmIntent)
            }
        }

    }
}