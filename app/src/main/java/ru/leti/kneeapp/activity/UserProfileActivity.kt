package ru.leti.kneeapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import ru.leti.kneeapp.R

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val medicalCardIdTextView: TextView = findViewById(R.id.textViewProfileMedicalCardIdValue)
        val emailTextView: TextView = findViewById(R.id.textViewProfileEmailValue)
        val phoneNumberTextView: TextView = findViewById(R.id.textViewProfilePhoneNumberValue)
        val firstNameTextView: TextView = findViewById(R.id.textViewProfileFirstNameValue)
        val lastNameTextView: TextView = findViewById(R.id.textViewProfileLastNameValue)
        val fatherNameTextView: TextView = findViewById(R.id.textViewProfileFatherNameValue)

        val extras = intent.extras
        val medicalCardId: String = extras?.getString("medicalCardId") ?: ""
        val email: String = extras?.getString("email") ?: ""
        val phoneNumber: String = extras?.getString("phoneNumber") ?: ""
        val firstName: String = extras?.getString("firstName") ?: ""
        val lastName: String = extras?.getString("lastName") ?: ""
        val fatherName: String = extras?.getString("fatherName") ?: ""

        medicalCardIdTextView.text = medicalCardId
        emailTextView.text = email
        phoneNumberTextView.text = phoneNumber
        firstNameTextView.text = firstName
        lastNameTextView.text = lastName
        fatherNameTextView.text = fatherName

    }
}