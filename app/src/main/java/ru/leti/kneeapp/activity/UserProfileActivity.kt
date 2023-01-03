package ru.leti.kneeapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import ru.leti.kneeapp.R

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val medicalCardIdTextView: TextView = findViewById(R.id.profile_medical_card_id_value_textview)
        val emailTextView: TextView = findViewById(R.id.profile_email_value_textview)
        val phoneNumberTextView: TextView = findViewById(R.id.profile_phone_number_value_textview)
        val firstNameTextView: TextView = findViewById(R.id.profile_first_name_value_textview)
        val lastNameTextView: TextView = findViewById(R.id.profile_last_name_value_textview)
        val fatherNameTextView: TextView = findViewById(R.id.profile_father_name_value_textview)

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