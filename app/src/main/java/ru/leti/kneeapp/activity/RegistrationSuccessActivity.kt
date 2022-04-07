package ru.leti.kneeapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import ru.leti.kneeapp.R

class RegistrationSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_success)

        val messageTextView : TextView = findViewById(R.id.successfullyRegisteredMessage)
        val buttonReturn : Button = findViewById(R.id.buttonBackToLoginPage)
        val extras = intent.extras
        val email: String = extras?.getString("email") ?: "null"
        val hiddenEmail: String
        val chars = email.toCharArray()
        if (email.indexOf('@') > 1)
            for (i in 2 until email.indexOf('@'))
                chars[i] = '*'
        else
            for (i in 0 until email.indexOf('@'))
                chars[i] = '*'
        hiddenEmail = String(chars)
        val messageToShow = getString(R.string.sent_email_verification_part_1) +
                hiddenEmail + getString(R.string.sent_email_verification_part_2)
        messageTextView.text = messageToShow
        buttonReturn.setOnClickListener {
            openLoginActivity()
        }
    }

    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}