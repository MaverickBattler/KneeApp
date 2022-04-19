package ru.leti.kneeapp.activity

import android.app.TaskStackBuilder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.leti.kneeapp.R

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        openLoginActivity()
    }

    private fun openLoginActivity() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        TaskStackBuilder.create(applicationContext).addNextIntentWithParentStack(loginIntent).startActivities()
    }
}