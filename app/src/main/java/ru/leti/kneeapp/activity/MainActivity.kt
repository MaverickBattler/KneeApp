package ru.leti.kneeapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ru.leti.kneeapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startOKSButton: Button = findViewById(R.id.buttonRunOKS)
        startOKSButton.setOnClickListener {
            openOksTestActivity()
        }
    }

    private fun openOksTestActivity() {
        val intent = Intent(this, TestPageActivity::class.java)
        startActivity(intent)
    }
}