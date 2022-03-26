package ru.leti.kneeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openLoginActivity()
        val startOKSButton: Button = findViewById(R.id.buttonRunOKS)
        startOKSButton.setOnClickListener {
            openOksTestActivity()
        }
    }

    private fun openOksTestActivity() {
        val intent = Intent(this, TestPageActivity::class.java)
        startActivity(intent)
    }

    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}