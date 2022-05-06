package ru.leti.kneeapp

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.leti.kneeapp.activity.StartActivity
import ru.leti.kneeapp.util.SharedPreferencesProvider

class AlarmReceiverNotificationOks : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val i = Intent(context, StartActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, i,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        context?.let {
            val sharedPreferencesProvider = SharedPreferencesProvider(context)
            val sharedPreferences = sharedPreferencesProvider.getEncryptedSharedPreferences()
            val editor = sharedPreferences.edit()
            editor.putString(
                "exercise1_1_pass_mark",
                context.getString(R.string.not_allowed_to)
            )
            editor.putString(
                "exercise1_2_pass_mark",
                context.getString(R.string.not_allowed_to)
            )
            editor.putString(
                "exercise1_3_pass_mark",
                context.getString(R.string.not_allowed_to)
            )
            editor.putString(
                "exercise2_1_pass_mark",
                context.getString(R.string.not_allowed_to)
            )
            editor.putString(
                "exercise3_1_pass_mark",
                context.getString(R.string.not_allowed_to)
            )
            editor.putString(
                "exercise3_2_pass_mark",
                context.getString(R.string.not_allowed_to)
            )
            editor.putString(
                "exercise3_3_pass_mark",
                context.getString(R.string.not_allowed_to)
            )
            editor.putString(
                "exercise4_1_pass_mark",
                context.getString(R.string.not_allowed_to)
            )
            editor.putString(
                "exercise4_2_1_pass_mark",
                context.getString(R.string.not_allowed_to)
            )
            editor.putString(
                "exercise4_2_2_pass_mark",
                context.getString(R.string.not_allowed_to)
            )
            editor.putString(
                "exercise4_2_3_pass_mark",
                context.getString(R.string.not_allowed_to)
            )
            editor.putString(
                "exercise4_3_1_pass_mark",
                context.getString(R.string.not_allowed_to)
            )
            editor.putString(
                "exercise4_3_2_pass_mark",
                context.getString(R.string.not_allowed_to)
            )
            editor.apply()

            val builder = NotificationCompat.Builder(it, it.getString(R.string.channel_id))
                .setContentTitle("KneeApp")
                .setContentText("Доступно еженедельное прохождение анкеты OKS")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
            with(NotificationManagerCompat.from(it)) {
                // notificationId is a unique int for each notification
                notify(1351, builder.build())
            }
        }
    }
}